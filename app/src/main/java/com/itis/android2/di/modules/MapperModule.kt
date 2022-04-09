package com.itis.android2.di.modules

import com.itis.android2.data.api.mappers.WeatherMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Provides
    fun provideWeatherMapper(): WeatherMapper = WeatherMapper()
}
