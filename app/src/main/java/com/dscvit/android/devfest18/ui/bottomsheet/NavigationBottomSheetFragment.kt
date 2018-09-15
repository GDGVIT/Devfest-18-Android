package com.dscvit.android.devfest18.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_bottom_sheet_navigation.*

class NavigationBottomSheetFragment : RoundedBottomSheetDialogFragment() {

    var navClickListener: NavClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        updateView(MainActivity.selectedFragmentIndex)
    }

    private fun setupViews() {
        layout_nav_main.setOnClickListener {
            navClickListener?.onNavItemClicked(0)
        }
        layout_nav_agenda.setOnClickListener {
            navClickListener?.onNavItemClicked(1)
        }
        layout_nav_sponsors.setOnClickListener {
            navClickListener?.onNavItemClicked(2)
        }
        layout_nav_about.setOnClickListener {
            navClickListener?.onNavItemClicked(3)
        }
        resetBackgrounds()
    }

    private fun resetBackgrounds() {
        text_nav_main.setBackgroundResource(0)
        text_nav_agenda.setBackgroundResource(0)
        text_nav_sponsors.setBackgroundResource(0)
        text_nav_about.setBackgroundResource(0)
    }

    private fun updateView(index: Int) {
        val bgResourceId = R.drawable.bg_nav_sheet_selection
        val bgRes = ContextCompat.getDrawable(context!!, bgResourceId)
        when(index) {
            0 -> text_nav_main.background = bgRes
            1 -> text_nav_agenda.background = bgRes
            2 -> text_nav_sponsors.background = bgRes
            3 -> text_nav_about.background = bgRes
        }
    }
}

interface NavClickListener {
    fun onNavItemClicked(index: Int)
}
