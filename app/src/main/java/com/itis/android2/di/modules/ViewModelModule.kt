package com.itis.android2.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itis.android2.di.ViewModelKey
import com.itis.android2.presentation.viewModels.DetailedScreenViewModel
import com.itis.android2.presentation.viewModels.MainViewModel
import com.itis.android2.utils.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factory: AppViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(
        viewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailedScreenViewModel::class)
    fun bindsDetailedScreenViewModel(
        viewModel: DetailedScreenViewModel
    ): ViewModel
}
