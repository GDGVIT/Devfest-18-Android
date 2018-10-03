package com.dscvit.android.devfest18.ui.info.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.info.about.CardAdapter.Companion.MAX_ELEVATION_FACTOR
import java.util.ArrayList

class CardPagerAdapter : PagerAdapter(), CardAdapter {

    private var mViews = ArrayList<CardView?>()
    private var mData = ArrayList<CardItem>()
    private var mBaseElevation: Float = 0.toFloat()

    fun addCardItem(item: CardItem) {
        mViews.add(null)
        mData.add(item)
    }

    override var baseElevation: Float
        get() = mBaseElevation
        set(value) {}
    override var cardCount: Int
        get() = mData.size
        set(value) {}

    override fun getCardViewAt(position: Int): CardView? = mViews[position]


    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun getCount(): Int = mData.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
                .inflate(R.layout.item_about_card, container, false)
        container.addView(view)
        bind(mData[position], view)
        val cardView = view.findViewById(R.id.cardView) as CardView

        if (mBaseElevation == 0f) {
            mBaseElevation = cardView.cardElevation
        }

        cardView.maxCardElevation = mBaseElevation * MAX_ELEVATION_FACTOR
        mViews[position] = cardView
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
        mViews[position] = null
    }

    private fun bind(item: CardItem, view: View) {
        val titleTextView = view.findViewById<View>(R.id.titleTextView) as TextView
        val contentTextView = view.findViewById<View>(R.id.contentTextView) as TextView
        titleTextView.setText(item.title)
        contentTextView.setText(item.text)
    }


}