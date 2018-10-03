package com.dscvit.android.devfest18.ui.info.about

import android.view.View

import androidx.viewpager.widget.ViewPager

class ShadowTransformer(private val mViewPager: ViewPager, private val mAdapter: CardAdapter) : ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    private var mLastOffset: Float = 0.toFloat()
    private var mScalingEnabled: Boolean = false

    init {
        mViewPager.addOnPageChangeListener(this)
    }

    fun enableScaling(enable: Boolean) {
        if (mScalingEnabled && !enable) {

            val currentCard = mAdapter.getCardViewAt(mViewPager.currentItem)
            if (currentCard != null) {
                currentCard.animate().scaleY(1f)
                currentCard.animate().scaleX(1f)
            }
        } else if (!mScalingEnabled && enable) {

            val currentCard = mAdapter.getCardViewAt(mViewPager.currentItem)
            if (currentCard != null) {
                currentCard.animate().scaleY(1.1f)
                currentCard.animate().scaleX(1.1f)
            }
        }

        mScalingEnabled = enable
    }

    override fun transformPage(page: View, position: Float) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val realCurrentPosition: Int
        val nextPosition: Int
        val baseElevation = mAdapter.baseElevation
        val realOffset: Float
        val goingLeft = mLastOffset > positionOffset

        if (goingLeft) {
            realCurrentPosition = position + 1
            nextPosition = position
            realOffset = 1 - positionOffset
        } else {
            nextPosition = position + 1
            realCurrentPosition = position
            realOffset = positionOffset
        }

        if (nextPosition > mAdapter.cardCount - 1 || realCurrentPosition > mAdapter.cardCount - 1) {
            return
        }

        val currentCard = mAdapter.getCardViewAt(realCurrentPosition)

        if (currentCard != null) {
            if (mScalingEnabled) {
                currentCard.scaleX = (1 + 0.1 * (1 - realOffset)).toFloat()
                currentCard.scaleY = (1 + 0.1 * (1 - realOffset)).toFloat()
            }
            currentCard.cardElevation = baseElevation + (baseElevation
                    * (CardAdapter.MAX_ELEVATION_FACTOR - 1).toFloat() * (1 - realOffset))
        }

        val nextCard = mAdapter.getCardViewAt(nextPosition)

        if (nextCard != null) {
            if (mScalingEnabled) {
                nextCard.scaleX = (1 + 0.1 * realOffset).toFloat()
                nextCard.scaleY = (1 + 0.1 * realOffset).toFloat()
            }
            nextCard.cardElevation = baseElevation + (baseElevation
                    * (CardAdapter.MAX_ELEVATION_FACTOR - 1).toFloat() * realOffset)
        }

        mLastOffset = positionOffset
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}

