package com.itis.android2.domain.models

data class WeatherDetail(
    val id: Int,
    val name: String,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Int,
    val deg: Int,
    val speed: Double,
    val description: String,
    val icon: String,
)
