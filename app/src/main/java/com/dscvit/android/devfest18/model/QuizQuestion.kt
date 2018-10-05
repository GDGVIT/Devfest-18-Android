package com.dscvit.android.devfest18.model

data class QuizQuestion(
        var question: String = "",
        var answer: String = "",
        var optionList: List<String> = listOf("")
)