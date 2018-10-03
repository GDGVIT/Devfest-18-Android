package com.dscvit.android.devfest18.ui.adapter


import androidx.cardview.widget.CardView

interface CardAdapter {

    var baseElevation: Float

    var cardCount: Int

    fun getCardViewAt(position: Int): CardView?

    companion object {
        const val MAX_ELEVATION_FACTOR = 8
    }
}