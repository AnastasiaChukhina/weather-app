package com.itis.android2.domain.converters

private const val BASE_IMAGE_URL = "http://openweathermap.org/img/wn/"
private const val IMAGE_URL_SUFFIX = "@2x.png"
private const val DEGREE_UNIT_UNICODE = "\u2103"
private const val PRESSURE_UNITS = "мм.рт.ст"
private const val WIND_SPEED_UNITS = "м/с"

class WeatherDataConverter {

    fun convertTempToString(temp: Double?): String = temp.toString() + DEGREE_UNIT_UNICODE

    fun convertPressureToString(pressure: Int?): String = pressure.toString() + PRESSURE_UNITS

    fun convertSpeedToString(speed: Double?): String = speed.toString() + WIND_SPEED_UNITS

    fun setImageIconUrl(iconCode: String?): String = BASE_IMAGE_URL + iconCode + IMAGE_URL_SUFFIX
}
