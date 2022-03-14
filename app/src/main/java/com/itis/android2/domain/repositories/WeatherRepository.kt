package com.itis.android2.domain.repositories

import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.models.WeatherSimple

interface WeatherRepository {
    suspend fun getWeatherByCity(name: String): WeatherDetail
    suspend fun getWeatherById(id: Int): WeatherDetail
    suspend fun getNearCitiesWeather(
        coordinates: Coord,
        count: Int
    ): MutableList<WeatherSimple>
}
