package com.itis.android2.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.itis.android2.presentation.MainActivity
import com.itis.android2.R
import com.itis.android2.data.api.mappers.WeatherMapper
import com.itis.android2.data.api.response.Coord
import com.itis.android2.data.repositories.LocationRepositoryImpl
import com.itis.android2.data.repositories.WeatherRepositoryImpl
import com.itis.android2.databinding.FragmentMainBinding
import com.itis.android2.domain.exceptions.GetWeatherException
import com.itis.android2.domain.usecases.location.GetLocationUseCase
import com.itis.android2.domain.usecases.weather.GetCityListUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByNameUseCase
import com.itis.android2.presentation.fragments.rv.WeatherAdapter
import com.itis.android2.presentation.fragments.rv.itemDecorators.SpaceItemDecorator
import kotlinx.coroutines.launch

private const val CITY_LIST_SIZE = 10

class MainFragment : Fragment(R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null
    private lateinit var coordinates: Coord

    private lateinit var getLocation: GetLocationUseCase
    private lateinit var getCityList: GetCityListUseCase
    private lateinit var getCityWeather: GetWeatherByNameUseCase

    private val weatherRepository by lazy {
        WeatherRepositoryImpl(WeatherMapper())
    }

    private val locationRepository by lazy {
        LocationRepositoryImpl(requireContext())
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            getLastLocation()
            initWeatherAdapter()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        setActionBarAttrs()
        initUseCases()
        initSearchView()
        decorateWeatherList()
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermissions())
            requestPermissions()
        getLastLocation()
        initWeatherAdapter()
    }

    private fun initUseCases() {
        getLocation = GetLocationUseCase(locationRepository)
        getCityList = GetCityListUseCase(weatherRepository)
        getCityWeather = GetWeatherByNameUseCase(weatherRepository)
    }

    private fun getLastLocation() {
        coordinates = getLocation()
    }

    private fun setActionBarAttrs() {
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHasOptionsMenu(false)
            title = getString(R.string.app_name)
        }
    }

    private fun decorateWeatherList() {
        binding?.rvCities?.apply {
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
            addItemDecoration(SpaceItemDecorator(context))
        }
    }

    private fun checkInputData(city: String?) {
        city?.let {
            lifecycleScope.launch {
                try {
                    val weatherResponse = getCityWeather(city)
                    showCityFragment(weatherResponse.id)
                } catch (e: GetWeatherException) {
                    showMessage("Город не найден")
                }
            }
        }
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun checkPermissions(): Boolean {
        activity?.apply {
            return (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED)
        }
        return false
    }

    private fun initWeatherAdapter() {
        lifecycleScope.launch {
            binding?.rvCities?.apply {
                adapter = WeatherAdapter(
                    weatherRepository.getNearCitiesWeather(coordinates, CITY_LIST_SIZE)
                ) {
                    showCityFragment(it)
                }
            }
        }
    }

    private fun initSearchView() {
        binding?.svCity?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                checkInputData(query?.trim())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    private fun showCityFragment(id: Int) {
        val bundle = Bundle().apply {
            putInt("ARG_CITY_ID", id)
        }
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.enter_from_right)
            .setExitAnim(R.anim.exit_to_left)
            .setPopEnterAnim(R.anim.enter_from_left)
            .setPopExitAnim(R.anim.exit_to_right)
            .build()

        findNavController().navigate(
            R.id.action_mainFragment_to_cityFragment,
            bundle,
            options
        )
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
