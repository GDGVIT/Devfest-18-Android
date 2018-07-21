package com.dscvit.android.devfest18.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.dscvit.android.devfest18.di.key.ViewModelKey
import com.dscvit.android.devfest18.ui.FactoryViewModel
import com.dscvit.android.devfest18.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: FactoryViewModel): ViewModelProvider.Factory

}