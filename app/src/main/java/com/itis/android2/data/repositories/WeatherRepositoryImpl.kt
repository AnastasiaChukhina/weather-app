package com.itis.android2.data.repositories

import com.itis.android2.BuildConfig
import com.itis.android2.data.api.Api
import com.itis.android2.data.api.mappers.WeatherMapper
import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.exceptions.GetCityListException
import com.itis.android2.domain.exceptions.GetWeatherException
import com.itis.android2.domain.models.WeatherDetail
import com.itis.android2.domain.models.WeatherSimple
import com.itis.android2.domain.repositories.WeatherRepository
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

class WeatherRepositoryImpl(
    private val mapper: WeatherMapper
) : WeatherRepository {

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
