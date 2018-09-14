package com.dscvit.android.devfest18.di.module

import com.dscvit.android.devfest18.ui.agenda.AgendaFragment
import com.dscvit.android.devfest18.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributesMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeAgendaFragment(): AgendaFragment

}