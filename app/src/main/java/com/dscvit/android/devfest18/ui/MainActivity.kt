package com.dscvit.android.devfest18.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.agenda.AgendaFragment
import com.dscvit.android.devfest18.ui.bottomsheet.NavClickListener
import com.dscvit.android.devfest18.ui.bottomsheet.NavigationBottomSheetFragment
import com.dscvit.android.devfest18.ui.info.about.AboutFragment
import com.dscvit.android.devfest18.ui.main.MainFragment
import com.dscvit.android.devfest18.ui.quiz.QuizFragment
import com.dscvit.android.devfest18.ui.scratch.ScratchFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, NavClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private val navigationBottomSheetFragment = NavigationBottomSheetFragment()

    companion object {
        var selectedFragmentIndex = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottom_app_bar)

        bottom_app_bar.title = "Bleh"

        if (savedInstanceState == null) {
            updateFragment(selectedFragmentIndex)
        }

        bottom_app_bar.setNavigationOnClickListener {
            showBottomSheet()
        }

        bottom_app_bar.setOnTouchListener { _, event ->
            val action: Int = MotionEventCompat.getActionMasked(event)
            when(action) {
                MotionEvent.ACTION_UP -> {
                    showBottomSheet()
                    return@setOnTouchListener true
                }
                else -> super.onTouchEvent(event)
            }
        }
    }

    private fun showBottomSheet() {
        navigationBottomSheetFragment.navClickListener = this
        navigationBottomSheetFragment.show(supportFragmentManager, navigationBottomSheetFragment.tag)
    }

    private fun updateFragment(index: Int) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, when(index) {
                    0 -> MainFragment.newInstance()
                    1 -> AgendaFragment.newInstance()
                    2 -> ScratchFragment.newInstance()
                    else -> AboutFragment.newInstance()
                })
                .commitNow()
    }

    override fun onNavItemClicked(index: Int) {
        navigationBottomSheetFragment.dismiss()
        selectedFragmentIndex = index
        updateFragment(index)
    }

}
