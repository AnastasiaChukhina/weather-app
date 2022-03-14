package com.itis.android2.domain.usecases.weather

import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.repositories.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWeatherByIdUseCase(
    private val weatherRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(id: Int): WeatherDetail {
        return withContext(dispatcher) {
            weatherRepository.getWeatherById(id)
        }
    }
}
