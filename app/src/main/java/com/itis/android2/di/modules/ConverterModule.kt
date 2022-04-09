package com.itis.android2.di.modules

import com.itis.android2.domain.converters.WeatherDataConverter
import com.itis.android2.domain.converters.WindDirectionsConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ConverterModule {

    @Provides
    fun provideWeatherDataHandler(): WeatherDataConverter = WeatherDataConverter()

    @Provides
    fun provideWindDirectionsConverter(): WindDirectionsConverter = WindDirectionsConverter()
}
