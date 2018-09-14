package com.dscvit.android.devfest18.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dscvit.android.devfest18.R

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun String.getResId(context: Context) = context.resources.getIdentifier(
        this,
        "drawable",
        context.packageName
)

fun String.getColourId(context: Context) = when(this) {
    "yellow" -> context.resources.getColor(R.color.yellow)
    "teal" -> context.resources.getColor(R.color.teal)
    "indigo" -> context.resources.getColor(R.color.indigo)
    "light_blue" -> context.resources.getColor(R.color.light_blue)
    "orange" -> context.resources.getColor(R.color.orange)
    else -> context.resources.getColor(R.color.indigo_dark)
}