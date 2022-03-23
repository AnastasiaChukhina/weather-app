package com.itis.android2.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itis.android2.domain.usecases.location.GetLocationUseCase
import com.itis.android2.domain.usecases.weather.GetCityListUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByIdUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByNameUseCase
import com.itis.android2.presentation.viewModels.DetailedScreenViewModel
import com.itis.android2.presentation.viewModels.MainViewModel

class ViewModelFactory(
    private val getCityListUseCase: GetCityListUseCase,
    private val getWeatherByNameUseCase: GetWeatherByNameUseCase,
    private val getWeatherByIdUseCase: GetWeatherByIdUseCase,
    private val getLocationUseCase: GetLocationUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(
                    getCityListUseCase,
                    getWeatherByNameUseCase,
                    getLocationUseCase
                ) as? T ?: throw IllegalArgumentException("Unknown ViewModel class")
            modelClass.isAssignableFrom(DetailedScreenViewModel::class.java) ->
                DetailedScreenViewModel(
                    getWeatherByIdUseCase
                ) as? T ?: throw IllegalArgumentException("Unknown ViewModel class")
            else ->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
