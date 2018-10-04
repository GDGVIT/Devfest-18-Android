package com.dscvit.android.devfest18.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.agenda.AgendaFragment
import com.dscvit.android.devfest18.ui.bottomsheet.NavClickListener
import com.dscvit.android.devfest18.ui.bottomsheet.NavigationBottomSheetFragment
import com.dscvit.android.devfest18.ui.info.about.AboutFragment
import com.dscvit.android.devfest18.ui.info.sponsors.SponsorFragment
import com.dscvit.android.devfest18.ui.question.QuestionFragment
import com.dscvit.android.devfest18.ui.quiz.QuizFragment
import com.dscvit.android.devfest18.ui.scratch.ScratchFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavClickListener {

    private val navigationBottomSheetFragment = NavigationBottomSheetFragment()

    companion object {
        var selectedFragmentIndex = 1
    }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { super.attachBaseContext(ViewPumpContextWrapper.wrap(it)) }
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

//        bottom_app_bar.setOnTouchListener { _, event ->
//            val action: Int = MotionEventCompat.getActionMasked(event)
//            when(action) {
//                MotionEvent.ACTION_UP -> {
//                    showBottomSheet()
//                    return@setOnTouchListener true
//                }
//                else -> super.onTouchEvent(event)
//            }
//        }
    }

    private fun showBottomSheet() {
        navigationBottomSheetFragment.navClickListener = this
        navigationBottomSheetFragment.show(supportFragmentManager, navigationBottomSheetFragment.tag)
    }

    private fun updateFragment(index: Int) {
        fab_main_add.hide()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, when(index) {
                    1 -> AgendaFragment.newInstance()
                    2 -> ScratchFragment.newInstance()
                    3 -> QuizFragment.newInstance()
                    4 -> SponsorFragment.newInstance()
                    5 -> {
                        fab_main_add.show()
                        QuestionFragment.newInstance()
                    }
                    6 -> AboutFragment.newInstance()
                    else -> SponsorFragment.newInstance()
                })
                .commitNow()
    }

    override fun onNavItemClicked(index: Int) {
        navigationBottomSheetFragment.dismiss()
        selectedFragmentIndex = index
        updateFragment(index)
    }

}
