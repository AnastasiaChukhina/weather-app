package com.itis.android2.data.api.response

data class CitiesWeatherResponse(
    val cod: String,
    val count: Int,
    val list: List<NearCityWeather>,
    val message: String
)
