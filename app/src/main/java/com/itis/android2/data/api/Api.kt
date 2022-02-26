package com.itis.android2.data.api

import com.itis.android2.data.api.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather?units=metric")
    suspend fun getWeather(@Query("q") city: String): WeatherResponse
}
