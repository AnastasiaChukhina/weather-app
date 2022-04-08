package com.itis.android2.di.modules

import com.itis.android2.BuildConfig
import com.itis.android2.data.api.Api
import com.itis.android2.di.modules.qualifiers.ApiKey
import com.itis.android2.di.modules.qualifiers.ApiLanguage
import com.itis.android2.di.modules.qualifiers.ApiUnits
import com.itis.android2.di.modules.qualifiers.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Provides
    @ApiKey
    fun providesApiKeyInterceptor() = Interceptor { chain ->
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

    @Provides
    @ApiUnits
    fun providesApiUnitsInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_UNITS_VALUE, API_UNITS)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    @Provides
    @ApiLanguage
    fun provideApiLangInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_LOCALE_VALUE, API_LOCALE)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    @Provides
    @Logger
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
    }

    @Provides
    fun providesOkhttp(
        @ApiKey apiKeyInterceptor: Interceptor,
        @ApiUnits apiUnitsInterceptor: Interceptor,
        @ApiLanguage apiLanguageInterceptor: Interceptor,
        @Logger loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(apiUnitsInterceptor)
            .addInterceptor(apiLanguageInterceptor)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(loggingInterceptor)
                }
            }
            .build()

    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun providesApi(
        okHttpClient: OkHttpClient,
        gsonConverter: GsonConverterFactory
    ): Api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .build()
            .create(Api::class.java)
}
