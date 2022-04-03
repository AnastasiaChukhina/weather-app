package com.itis.android2.data.repositories

import com.itis.android2.data.api.Api
import com.itis.android2.data.api.mappers.WeatherMapper
import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.exceptions.GetCityListException
import com.itis.android2.domain.exceptions.GetWeatherException
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.models.WeatherSimple
import com.itis.android2.domain.repositories.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: Api,
    private val mapper: WeatherMapper
) : WeatherRepository {

    override suspend fun getWeatherByCity(name: String): WeatherDetail {
        try {
            return mapper.mapWeatherDetail(api.getWeatherByCity(name))
        } catch (e: RuntimeException) {
            throw GetWeatherException("Can't get weather by city", e)
        }
    }

    override suspend fun getWeatherById(id: Int): WeatherDetail {
        try {
            return mapper.mapWeatherDetail(api.getWeatherById(id))
        } catch (e: RuntimeException) {
            throw GetWeatherException("Can't get weather by id", e)
        }
    }

    override suspend fun getNearCitiesWeather(
        coordinates: Coord,
        count: Int
    ): MutableList<WeatherSimple> {
        try {
            val list: MutableList<WeatherSimple> = mutableListOf()
            api.getNearCitiesWeather(coordinates.lat, coordinates.lon, count).list.forEach {
                list.add(mapper.mapWeatherSimple(it))
            }
            return list
        } catch (e: RuntimeException) {
            throw GetCityListException("Can't get weather list", e)
        }
    }
}
