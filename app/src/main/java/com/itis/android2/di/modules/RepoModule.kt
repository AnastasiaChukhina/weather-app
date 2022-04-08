package com.itis.android2.di.modules

import com.itis.android2.data.repositories.LocationRepositoryImpl
import com.itis.android2.data.repositories.WeatherRepositoryImpl
import com.itis.android2.domain.repositories.LocationRepository
import com.itis.android2.domain.repositories.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepoModule {

    @Binds
    fun weatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    fun locationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}
