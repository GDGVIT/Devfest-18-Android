package com.dscvit.android.devfest18.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.dscvit.android.devfest18.network.WebService
import com.dscvit.android.devfest18.utils.Constants
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providePreferences(application: Application): SharedPreferences = application.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideWebService(): WebService = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(WebService::class.java)
}