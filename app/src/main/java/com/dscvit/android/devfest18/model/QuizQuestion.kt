package com.dscvit.android.devfest18.model

data class QuizQuestion(
        var question: String = "question",
        var answer: String = "answer op1",
        var optionList: List<String> = listOf("op1", "op1", "op1", "op1")
)