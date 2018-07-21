package com.dscvit.android.devfest18.di.module

import com.dscvit.android.devfest18.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contribuesMainActivity(): MainActivity

}