package com.dscvit.android.devfest18.di

import android.app.Application
import com.dscvit.android.devfest18.DevfestApp
import com.dscvit.android.devfest18.di.module.ActivityModule
import com.dscvit.android.devfest18.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(devfestApp: DevfestApp)

}