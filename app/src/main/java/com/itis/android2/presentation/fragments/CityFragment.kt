package com.itis.android2.presentation.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.itis.android2.R
import com.itis.android2.databinding.FragmentCityBinding
import com.itis.android2.domain.converters.WeatherDataConverter
import com.itis.android2.domain.converters.WindDirectionsConverter
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.presentation.MainActivity
import com.itis.android2.presentation.viewModels.DetailedScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityFragment : Fragment(R.layout.fragment_city) {

    private lateinit var binding: FragmentCityBinding

    @Inject
    lateinit var dataConverter: WeatherDataConverter

    @Inject
    lateinit var windConverter: WindDirectionsConverter

    private val viewModel: DetailedScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)

        initObservers()
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
            ivWeatherIcon.load(dataConverter.setImageIconUrl(weather.icon))
            tvFeelsLikeValue.text = dataConverter.convertTempToString(weather.feelsLike)
            tvPressureValue.text = dataConverter.convertPressureToString(weather.pressure)
            tvWeatherState.text = weather.description
            tvCityTempValue.text = dataConverter.convertTempToString(weather.temp)
            tvWindDirectionValue.text = windConverter.convertWindToString(weather.deg)
            tvWindSpeedValue.text = dataConverter.convertSpeedToString(weather.speed)
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

    private fun showMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}
