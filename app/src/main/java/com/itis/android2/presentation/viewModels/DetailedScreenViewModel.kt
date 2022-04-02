package com.itis.android2.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.usecases.weather.GetWeatherByIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailedScreenViewModel @Inject constructor(
    private val getWeatherByIdUseCase: GetWeatherByIdUseCase
): ViewModel() {

    private var _weatherDetail: MutableLiveData<Result<WeatherDetail>> = MutableLiveData()
    val weatherDetail: LiveData<Result<WeatherDetail>> = _weatherDetail

    fun getWeatherById(id: Int) {
        viewModelScope.launch {
            try {
                val weather = getWeatherByIdUseCase(id)
                _weatherDetail.value = Result.success(weather)
            } catch (ex: Exception) {
                _weatherDetail.value = Result.failure(ex)
            }
        }
    }
}
