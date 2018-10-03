package com.dscvit.android.devfest18.utils.ui

import android.widget.ProgressBar
import android.view.animation.Animation
import android.view.animation.Transformation


class ProgressBarAnimation (private val progressBar: ProgressBar, val from: Float, val to: Float) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
    }
}