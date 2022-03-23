package com.itis.android2.presentation.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.itis.android2.MainActivity
import com.itis.android2.R
import com.itis.android2.data.api.mappers.WeatherMapper
import com.itis.android2.data.repositories.LocationRepositoryImpl
import com.itis.android2.data.repositories.WeatherRepositoryImpl
import com.itis.android2.databinding.FragmentCityBinding
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.helpers.WeatherDataHandler
import com.itis.android2.domain.usecases.location.GetLocationUseCase
import com.itis.android2.domain.usecases.weather.GetCityListUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByIdUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByNameUseCase
import com.itis.android2.presentation.viewModels.DetailedScreenViewModel
import com.itis.android2.utils.ViewModelFactory

class CityFragment : Fragment(R.layout.fragment_city) {

    private lateinit var binding: FragmentCityBinding
    private lateinit var viewModel: DetailedScreenViewModel
    private lateinit var windDirections: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)

        initObjects()
        initObservers()
        getWindDirections()
        getWeatherData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                returnToMainFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        )[DetailedScreenViewModel::class.java]
    }


    private fun initObservers() {
        viewModel.weatherDetail.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { weatherData ->
                initAttrs(weatherData)
            }, onFailure = {
                showMessage("Не удается получить данные о погоде.")
            })
        }
    }

    private fun getWeatherData() {
        arguments?.getInt("ARG_CITY_ID")?.let {
            viewModel.getWeatherById(it)
        }
    }

    private fun initAttrs(weather: WeatherDetail) {
        initActionBarAttrs(weather.name)

        binding.apply {
            ivWeatherIcon.load(WeatherDataHandler.setImageIconUrl(weather.icon))
            tvFeelsLikeValue.text = WeatherDataHandler.convertTempToString(weather.feelsLike)
            tvPressureValue.text = WeatherDataHandler.convertPressureToString(weather.pressure)
            tvWeatherState.text = weather.description
            tvCityTempValue.text = WeatherDataHandler.convertTempToString(weather.temp)
            tvWindDirectionValue.text = windDirections.let {
                WeatherDataHandler.convertWindDegreeToString(
                    weather.deg,
                    it
                )
            }
            tvWindSpeedValue.text = WeatherDataHandler.convertSpeedToString(weather.speed)
        }
    }

    private fun initActionBarAttrs(name: String) {
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
            title = name
        }
    }

    private fun returnToMainFragment() {
        findNavController().popBackStack()
    }

    private fun getWindDirections() {
        windDirections = listOf(
            getString(R.string.wind_n),
            getString(R.string.wind_ne),
            getString(R.string.wind_e),
            getString(R.string.wind_se),
            getString(R.string.wind_s),
            getString(R.string.wind_sw),
            getString(R.string.wind_w),
            getString(R.string.wind_nw)
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
