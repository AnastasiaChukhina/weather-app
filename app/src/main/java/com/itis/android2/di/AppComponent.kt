package com.itis.android2.di

import com.itis.android2.App
import com.itis.android2.presentation.MainActivity
import com.itis.android2.di.modules.*
import com.itis.android2.presentation.fragments.CityFragment
import com.itis.android2.presentation.fragments.MainFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        AppModule::class,
        MapperModule::class,
        NetModule::class,
        RepoModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(cityFragment: CityFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }
}
