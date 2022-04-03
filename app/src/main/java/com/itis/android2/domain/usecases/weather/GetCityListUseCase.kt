package com.itis.android2.domain.usecases.weather

import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.models.WeatherSimple
import com.itis.android2.domain.repositories.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCityListUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(coordinates: Coord, count: Int): MutableList<WeatherSimple> {
        return withContext(dispatcher) {
            weatherRepository.getNearCitiesWeather(coordinates, count)
        }
    }
}
