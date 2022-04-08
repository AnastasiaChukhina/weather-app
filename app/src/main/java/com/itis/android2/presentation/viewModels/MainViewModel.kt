package com.itis.android2.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.models.WeatherSimple
import com.itis.android2.domain.usecases.location.GetLocationUseCase
import com.itis.android2.domain.usecases.weather.GetCityListUseCase
import com.itis.android2.domain.usecases.weather.GetWeatherByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCityListUseCase: GetCityListUseCase,
    private val getWeatherByNameUseCase: GetWeatherByNameUseCase,
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private var _weatherList: MutableLiveData<Result<MutableList<WeatherSimple>>> =
        MutableLiveData()
    val weatherList: LiveData<Result<MutableList<WeatherSimple>>> = _weatherList

    private var _weatherDetail: SingleLiveEvent<Result<WeatherDetail>> = SingleLiveEvent()
    val weatherDetail: SingleLiveEvent<Result<WeatherDetail>> = _weatherDetail

    private var _location: MutableLiveData<Result<Coord>> = MutableLiveData()
    val location: LiveData<Result<Coord>> = _location

    fun getCityList(coordinates: Coord, count: Int) {
        viewModelScope.launch {
            try {
                val weatherList = getCityListUseCase(coordinates, count)
                _weatherList.value = Result.success(weatherList)
            } catch (ex: Exception) {
                _weatherList.value = Result.failure(ex)
            }
        }
    }

    fun getDetailedWeatherByName(name: String) {
        viewModelScope.launch {
            try {
                val weatherDetail = getWeatherByNameUseCase(name)
                _weatherDetail.value = Result.success(weatherDetail)
            } catch (ex: Exception) {
                _weatherDetail.value = Result.failure(ex)
            }
        }
    }

    fun getLocation() {
        viewModelScope.launch {
            try {
                val location = getLocationUseCase()
                _location.value = Result.success(location)
            } catch (ex: Exception) {
                _location.value = Result.failure(ex)
            }
        }
    }
}
