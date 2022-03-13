package com.itis.android2.presentation.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.itis.android2.presentation.MainActivity
import com.itis.android2.R
import com.itis.android2.data.api.mappers.WeatherMapper
import com.itis.android2.data.repositories.WeatherRepositoryImpl
import com.itis.android2.databinding.FragmentCityBinding
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.helpers.WeatherDataHandler
import com.itis.android2.domain.usecases.weather.GetWeatherByIdUseCase
import kotlinx.coroutines.launch

class CityFragment : Fragment(R.layout.fragment_city) {

    private var binding: FragmentCityBinding? = null
    private lateinit var getCityWeather: GetWeatherByIdUseCase
    private lateinit var windDirections: List<String>

    private val weatherRepository by lazy {
        WeatherRepositoryImpl(WeatherMapper())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)
        initUseCases()
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

    private fun initUseCases() {
        getCityWeather = GetWeatherByIdUseCase(weatherRepository)
    }

    private fun getWeatherData() {
        arguments?.getInt("ARG_CITY_ID")?.let {
            setWeatherData(it)
        }
    }

    private fun initActionBarAttrs(name: String) {
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
            title = name
        }
    }

    private fun setWeatherData(id: Int) {
        lifecycleScope.launch {
            initAttrs(getCityWeather(id))
        }
    }

    private fun initAttrs(weather: WeatherDetail) {
        initActionBarAttrs(weather.name)

        binding?.apply {
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

    private fun returnToMainFragment() {
        findNavController().navigateUp()
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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
