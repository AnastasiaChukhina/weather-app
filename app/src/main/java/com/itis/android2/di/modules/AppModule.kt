package com.itis.android2.di.modules

import android.content.Context
import com.itis.android2.App
import com.itis.android2.domain.helpers.WeatherDataHandler
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    fun provideWeatherDataHandler(): WeatherDataHandler = WeatherDataHandler()

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
