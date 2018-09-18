package com.dscvit.android.devfest18.model

data class Quiz(
        var quizEnabled: Boolean = false,
        var quizList: List<QuizQuestion> = listOf(QuizQuestion(), QuizQuestion(), QuizQuestion(), QuizQuestion())
)