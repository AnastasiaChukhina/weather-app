package com.itis.android2.presentation.viewModels

import androidx.lifecycle.*
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.usecases.weather.GetWeatherByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailedScreenViewModel @AssistedInject constructor(
    @Assisted private val cityId: Int,
    private val getWeatherByIdUseCase: GetWeatherByIdUseCase
) : ViewModel() {

    private var _weatherDetail: MutableLiveData<Result<WeatherDetail>> = MutableLiveData()
    val weatherDetail: LiveData<Result<WeatherDetail>> = _weatherDetail

    fun getWeatherById() {
        viewModelScope.launch {
            try {
                val weather = getWeatherByIdUseCase(cityId)
                _weatherDetail.value = Result.success(weather)
            } catch (ex: Exception) {
                _weatherDetail.value = Result.failure(ex)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(cityId: Int): DetailedScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            cityId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                assistedFactory.create(cityId) as T
        }
    }
}
