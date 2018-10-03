package com.dscvit.android.devfest18.ui.info.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.CardItem
import com.dscvit.android.devfest18.ui.adapter.CardPagerAdapter
import com.dscvit.android.devfest18.utils.ui.ShadowTransformer
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    private lateinit var mCardAdapter: CardPagerAdapter
    private lateinit var mCardShadowTransformer: ShadowTransformer

    companion object {
        fun newInstance() = AboutFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCardAdapter = CardPagerAdapter()
        mCardAdapter.addCardItem(CardItem(R.string.title_1, R.string.text_1))
        mCardAdapter.addCardItem(CardItem(R.string.title_2, R.string.text_2))
        mCardAdapter.addCardItem(CardItem(R.string.title_3, R.string.text_3))
        mCardAdapter.addCardItem(CardItem(R.string.title_4, R.string.text_4))
        mCardAdapter.addCardItem(CardItem(R.string.title_5, R.string.text_5))

        mCardShadowTransformer = ShadowTransformer(viewPager, mCardAdapter)

        viewPager.adapter = mCardAdapter
        viewPager.setPageTransformer(false, mCardShadowTransformer)
        viewPager.offscreenPageLimit = 3
    }
}