package com.dscvit.android.devfest18.model

data class Quiz(
        var quizEnabled: Long = 0,
        var quizList: List<QuizQuestion> = listOf(QuizQuestion(), QuizQuestion(), QuizQuestion(), QuizQuestion())
)