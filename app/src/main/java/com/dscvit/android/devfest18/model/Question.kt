package com.dscvit.android.devfest18.model

import java.util.*
import kotlin.collections.ArrayList

data class Question(
        var id: String = "",
        var upvotes: Long = 0.toLong(),
        var question: String = "",
//        var tagList: List<String> = listOf("tag1", "tag2"),
        var tag: String = "",
        var upVotedList: ArrayList<String> = arrayListOf(""),
        var date: Date = Date(),
        var userName: String = "",
        var userEmail: String = ""
)