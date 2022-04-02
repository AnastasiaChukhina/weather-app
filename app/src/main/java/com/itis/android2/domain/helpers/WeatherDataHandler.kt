package com.itis.android2.domain.helpers

import kotlin.math.roundToInt

private const val BASE_IMAGE_URL = "http://openweathermap.org/img/wn/"
private const val IMAGE_URL_SUFFIX = "@2x.png"
private const val DEGREE_UNIT_UNICODE = "\u2103"
private const val DEGREES = 360
private const val PRESSURE_UNITS = "мм.рт.ст"
private const val WIND_SPEED_UNITS = "м/с"

class WeatherDataHandler {

    fun convertTempToString(temp: Double?): String = temp.toString() + DEGREE_UNIT_UNICODE

    fun convertWindDegreeToString(degree: Int, directions: List<String>): String {
        val degrees = (degree * directions.size / DEGREES).toDouble().roundToInt()
        return directions[degrees % directions.size]
    }

    fun convertPressureToString(pressure: Int?): String = pressure.toString() + PRESSURE_UNITS

    fun convertSpeedToString(speed: Double?): String = speed.toString() + WIND_SPEED_UNITS

    fun setImageIconUrl(iconCode: String?): String = BASE_IMAGE_URL + iconCode + IMAGE_URL_SUFFIX
}
