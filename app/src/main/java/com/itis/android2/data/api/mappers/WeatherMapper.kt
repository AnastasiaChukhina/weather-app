package com.itis.android2.data.api.mappers

import com.itis.android2.data.api.response.NearCityWeather
import com.itis.android2.data.api.response.WeatherResponse
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.models.WeatherSimple

class WeatherMapper {

    fun mapWeatherSimple(response: NearCityWeather): WeatherSimple = WeatherSimple(
        response.id,
        response.name,
        response.main.temp,
        response.weather[0].icon
    )

    fun mapWeatherDetail(response: WeatherResponse): WeatherDetail = WeatherDetail(
        response.id,
        response.name,
        response.main.temp,
        response.main.feelsLike,
        response.main.pressure,
        response.wind.deg,
        response.wind.speed,
        response.weather[0].description,
        response.weather[0].icon
    )
}
