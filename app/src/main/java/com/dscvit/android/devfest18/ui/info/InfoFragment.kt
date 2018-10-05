package com.dscvit.android.devfest18.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.agenda.AgendaFragment
import com.dscvit.android.devfest18.ui.info.about.AboutFragment
import com.dscvit.android.devfest18.ui.info.faq.FaqFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_info.*


class InfoFragment : Fragment() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    companion object {

        fun newInstance(): Fragment {
            return InfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)
        about_container.adapter = mSectionsPagerAdapter

        about_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(about_tabs))
        about_tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(about_container))
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment  = when(position) {
            0 -> AboutFragment.newInstance()
            1 -> FaqFragment.newInstance()
            else -> FaqFragment.newInstance()
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }
    }
}
