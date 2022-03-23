package com.itis.android2.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.itis.android2.MainActivity
import com.itis.android2.R
import com.itis.android2.data.api.mappers.WeatherMapper
import com.itis.android2.data.api.response.Coord
import com.itis.android2.data.repositories.LocationRepositoryImpl
import com.itis.android2.data.repositories.WeatherRepositoryImpl
import com.itis.android2.databinding.FragmentMainBinding
import com.itis.android2.domain.usecases.location.GetLocationUseCase
import com.itis.android2.domain.usecases.weather.GetCityListUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByIdUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByNameUseCase
import com.itis.android2.presentation.viewModels.MainViewModel
import com.itis.android2.presentation.fragments.rv.WeatherAdapter
import com.itis.android2.presentation.fragments.rv.itemDecorators.SpaceItemDecorator
import com.itis.android2.utils.ViewModelFactory

private const val CITY_LIST_SIZE = 10

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private lateinit var coordinates: Coord
    private lateinit var viewModel: MainViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            viewModel.getLocation()
            viewModel.getCityList(coordinates, CITY_LIST_SIZE)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        weatherAdapter = WeatherAdapter { showCityFragment(it) }

        setActionBarAttrs()
        initObjects()
        initObservers()
        initSearchView()
        initWeatherList()
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermissions())
            requestPermissions()
        viewModel.getLocation()
        viewModel.getCityList(coordinates, CITY_LIST_SIZE)
    }

    private fun initObjects() {
        val weatherRepository = WeatherRepositoryImpl(WeatherMapper())

        val factory = ViewModelFactory(
            GetCityListUseCase(weatherRepository),
            GetWeatherByNameUseCase(weatherRepository),
            GetWeatherByIdUseCase(weatherRepository),
            GetLocationUseCase(LocationRepositoryImpl(requireContext()))
        )

        viewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]
    }

    private fun initObservers() {
        viewModel.location.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { result ->
                coordinates = result
            }, onFailure = {
                Log.e("Location", "Can't get location")
            })
        }
        viewModel.weatherList.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { result ->
                weatherAdapter.submitList(result)
            }, onFailure = {
                showMessage("Не удается получить погоду в ближайших населенных пунктах.")
            })
        }
        viewModel.weatherDetail.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { weatherData ->
                showCityFragment(weatherData.id)
            }, onFailure = {
                showMessage("Город не найден.")
            })
        }
    }

    private fun setActionBarAttrs() {
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHasOptionsMenu(false)
            title = getString(R.string.app_name)
        }
    }

    private fun initWeatherList() {
        binding.rvCities.apply {
            adapter = weatherAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
            addItemDecoration(SpaceItemDecorator(context))
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

    private fun initSearchView() {
        binding.svCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getDetailedWeatherByName(query?.trim().toString())
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
}
