package com.dscvit.android.devfest18.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dscvit.android.devfest18.R
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

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

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun TextView.setQuizOptionSelected() {
    this.setTextColor(resources.getColor(R.color.dark_black))
    this.backgroundResource = R.drawable.bg_quiz_option_selected
}

fun TextView.resetQuizOptionSelected() {
    this.setTextColor(resources.getColor(R.color.white))
    this.backgroundResource = R.drawable.bg_quiz_option
}

fun TextView.setCorrectIndication() {
    this.setTextColor(resources.getColor(R.color.dark_black))
    this.backgroundResource = R.drawable.bg_quiz_option_correct
}

fun TextView.setWrongIndication() {
    this.setTextColor(resources.getColor(R.color.white))
    this.backgroundResource = R.drawable.bg_quiz_option_wrong
}