package com.dscvit.android.devfest18.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.agenda.AgendaFragment
import com.dscvit.android.devfest18.ui.bottomsheet.NavClickListener
import com.dscvit.android.devfest18.ui.bottomsheet.NavigationBottomSheetFragment
import com.dscvit.android.devfest18.ui.main.MainFragment
import com.dscvit.android.devfest18.ui.quiz.QuizFragment
import com.dscvit.android.devfest18.ui.scratch.ScratchFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, NavClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private val navigationBottomSheetFragment = NavigationBottomSheetFragment()

    companion object {
        var selectedFragmentIndex = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        setSupportActionBar(bottom_app_bar)

        if (savedInstanceState == null) {
            updateFragment(selectedFragmentIndex)
        }

        bottom_app_bar.setNavigationOnClickListener {
            navigationBottomSheetFragment.navClickListener = this
            navigationBottomSheetFragment.show(supportFragmentManager, navigationBottomSheetFragment.tag)
        }
    }

    private fun updateFragment(index: Int) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, when(index) {
                    0 -> MainFragment.newInstance()
                    1 -> AgendaFragment.newInstance()
                    2 -> ScratchFragment.newInstance()
                    else -> QuizFragment.newInstance()
                })
                .commitNow()
    }

    override fun onNavItemClicked(index: Int) {
        navigationBottomSheetFragment.dismiss()
        selectedFragmentIndex = index
        updateFragment(index)
    }

}
