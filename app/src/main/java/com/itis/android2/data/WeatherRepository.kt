package com.itis.android2.data

import com.itis.android2.BuildConfig
import com.itis.android2.data.api.Api
import com.itis.android2.data.api.CitiesWeatherResponse
import com.itis.android2.data.api.WeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_KEY = "251c40b92d5a3380ef4a0836a8dd088a"
private const val QUERY_API_KEY = "appid"
private const val API_UNITS = "metric"
private const val QUERY_UNITS_VALUE = "units"
private const val API_LOCALE = "ru"
private const val QUERY_LOCALE_VALUE = "lang"

class WeatherRepository {

    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_API_KEY, API_KEY)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    private val apiUnitsInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_UNITS_VALUE, API_UNITS)
            .addQueryParameter(QUERY_LOCALE_VALUE, API_LOCALE)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(apiUnitsInterceptor)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(
                                HttpLoggingInterceptor.Level.BODY
                            )
                    )
                }
            }
            .build()
    }

    private val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    suspend fun getWeatherByCity(city: String): WeatherResponse {
        return api.getWeatherByCity(city)
    }

    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): WeatherResponse {
        return api.getWeatherByCoordinates(lat, lon)
    }

    suspend fun getWeatherById(id: Int): WeatherResponse {
        return api.getWeatherById(id)
    }

    suspend fun getNearCitiesWeather(
        lat: Double,
        lon: Double,
        count: Int
    ): CitiesWeatherResponse {
        return api.getNearCitiesWeather(lat, lon, count)
    }
}
