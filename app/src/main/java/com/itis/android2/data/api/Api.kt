package com.itis.android2.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    suspend fun getWeatherByCity(@Query("q") city: String): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude:Double
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherById(@Query("id") id: Int): WeatherResponse

    @GET("find")
    suspend fun getNearCitiesWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") count: Int
    ): CitiesWeatherResponse
}
