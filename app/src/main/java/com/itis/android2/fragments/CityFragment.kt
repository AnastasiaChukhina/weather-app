package com.itis.android2.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.itis.android2.MainActivity
import com.itis.android2.R
import com.itis.android2.data.WeatherRepository
import com.itis.android2.data.api.WeatherResponse
import com.itis.android2.databinding.FragmentCityBinding
import com.itis.android2.helpers.WeatherDataHandler
import kotlinx.coroutines.launch

class CityFragment : Fragment(R.layout.fragment_city) {

    private var binding: FragmentCityBinding? = null
    private var id: Int? = null
    private var windDirections: List<String>? = null

    private val repository by lazy {
        WeatherRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        arguments?.getInt("ARG_CITY_ID").let {
            id = it
        }
        id?.let {
            setWeatherData(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)
        getWindDirections()
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

    private fun initAttrs(weatherResponse: WeatherResponse){
        binding?.apply {
            ivWeatherIcon.load(
                WeatherDataHandler.setImageIconUrl(weatherResponse.weather[0].icon)
            )
            tvFeelsLikeValue.text = WeatherDataHandler.convertTempToString(weatherResponse.main.feelsLike)
            tvPressureValue.text = WeatherDataHandler.convertPressureToString(weatherResponse.main.pressure)
            tvWeatherState.text = weatherResponse.weather[0].description
            tvCityTempValue.text = WeatherDataHandler.convertTempToString(weatherResponse.main.temp)
            tvWindDirectionValue.text = windDirections?.let {
                WeatherDataHandler.convertWindDegreeToString(
                    weatherResponse.wind.deg,
                    it
                )
            }
            tvWindSpeedValue.text = WeatherDataHandler.convertSpeedToString(weatherResponse.wind.speed)
        }
        setTitle(weatherResponse.name)
    }

    private fun setTitle(name: String?) {
        (activity as MainActivity).supportActionBar?.apply {
            title = name
        }
    }

    private fun setWeatherData(id: Int) {
        lifecycleScope.launch {
            initAttrs(repository.getWeatherById(id))
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
