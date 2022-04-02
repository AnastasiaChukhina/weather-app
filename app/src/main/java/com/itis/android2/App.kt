package com.itis.android2

import android.app.Application
import com.itis.android2.di.AppComponent
import com.itis.android2.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}
