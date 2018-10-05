package com.dscvit.android.devfest18.model

data class Quiz(
        var quizEnabled: Long = 0.toLong(),
        var quizList: List<QuizQuestion> = listOf(QuizQuestion())
)