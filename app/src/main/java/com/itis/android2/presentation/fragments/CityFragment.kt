package com.itis.android2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.itis.android2.App
import com.itis.android2.presentation.MainActivity
import com.itis.android2.R
import com.itis.android2.databinding.FragmentCityBinding
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.helpers.WeatherDataHandler
import com.itis.android2.presentation.viewModels.DetailedScreenViewModel
import com.itis.android2.utils.AppViewModelFactory
import javax.inject.Inject

class CityFragment : Fragment(R.layout.fragment_city) {

    private lateinit var binding: FragmentCityBinding
    private lateinit var windDirections: List<String>

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var dataHandler: WeatherDataHandler

    private val viewModel: DetailedScreenViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)

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
            ivWeatherIcon.load(dataHandler.setImageIconUrl(weather.icon))
            tvFeelsLikeValue.text = dataHandler.convertTempToString(weather.feelsLike)
            tvPressureValue.text = dataHandler.convertPressureToString(weather.pressure)
            tvWeatherState.text = weather.description
            tvCityTempValue.text = dataHandler.convertTempToString(weather.temp)
            tvWindDirectionValue.text = windDirections.let {
                dataHandler.convertWindDegreeToString(
                    weather.deg,
                    it
                )
            }
            tvWindSpeedValue.text = dataHandler.convertSpeedToString(weather.speed)
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
