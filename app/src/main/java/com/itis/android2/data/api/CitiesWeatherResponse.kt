package com.itis.android2.data.api

import com.itis.android2.data.api.response.NearCityWeather

data class CitiesWeatherResponse(
    val cod: String,
    val count: Int,
    val list: List<NearCityWeather>,
    val message: String
)
