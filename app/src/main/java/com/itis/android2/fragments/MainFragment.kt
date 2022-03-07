package com.itis.android2.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.itis.android2.MainActivity
import com.itis.android2.R
import com.itis.android2.data.WeatherRepository
import com.itis.android2.databinding.FragmentMainBinding
import com.itis.android2.rv.WeatherAdapter
import com.itis.android2.rv.itemDecorators.SpaceItemDecorator
import kotlinx.coroutines.launch

private const val DEFAULT_LATITUDE = 55.7887
private const val DEFAULT_LONGITUDE = 49.1221
private const val CITY_LIST_SIZE = 10
private const val HTTP_STATUS_NOT_FOUND = 404

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var binding: FragmentMainBinding? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val repository by lazy {
        WeatherRepository()
    }

    @SuppressLint("MissingPermission")
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true || it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    latitude = location.latitude
                    longitude = location.longitude
                }
            } else{
                latitude = DEFAULT_LATITUDE
                longitude = DEFAULT_LONGITUDE
            }
            latitude?.let { it ->
                longitude?.let { it1 ->
                    initWeatherAdapter(it, it1, CITY_LIST_SIZE)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

        context?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }
        getLastLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setActionBarAttrs()

        binding?.apply {
            rvCities.run {
                addItemDecoration(
                    DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
                )
                addItemDecoration(SpaceItemDecorator(context))
            }
            svCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    checkInputData(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }
            })
        }
        getLastLocation()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

    private fun setActionBarAttrs() {
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.app_name)
        }
    }

    private fun checkInputData(city: String?) {
        city?.let {
            lifecycleScope.launch {
                try{
                    val weatherResponse = repository.getWeatherByCity(city)
                    showCityFragment(weatherResponse.id)
                } catch (e: RuntimeException){
                    showMessage("Город не найден")
                }
            }
        }
    }

    private fun getLastLocation() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun initWeatherAdapter(lat: Double, lon: Double, count: Int) {
        lifecycleScope.launch {
            binding?.rvCities?.apply {
                adapter = WeatherAdapter(repository.getNearCitiesWeather(lat, lon, count).list) {
                    showCityFragment(it)
                }
            }
        }
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
