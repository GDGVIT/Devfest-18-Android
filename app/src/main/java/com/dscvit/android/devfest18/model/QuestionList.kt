package com.dscvit.android.devfest18.model

data class QuestionList(
        var mainList: ArrayList<Question> = arrayListOf(Question(), Question()),
        var backupList: ArrayList<Question> = arrayListOf(Question(), Question())
)