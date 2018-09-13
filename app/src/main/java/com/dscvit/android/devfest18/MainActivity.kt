package com.dscvit.android.devfest18

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.android.devfest18.ui.main.MainFragment
import com.dscvit.android.devfest18.ui.main.bottomsheet.NavigationBottomSheetFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector()= dispatchingAndroidInjector

    private val naviBottomSheetFragment = NavigationBottomSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        setSupportActionBar(bottom_app_bar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        bottom_app_bar.setNavigationOnClickListener {
            naviBottomSheetFragment.show(supportFragmentManager, naviBottomSheetFragment.tag)
        }
    }

}
