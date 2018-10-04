package com.dscvit.android.devfest18.model

data class Question(
        var upvotes: Long = 2.toLong(),
        var question: String = "Make something retire instantly I'm chipping don't call me michael mayers on my vicinity",
        var tagList: List<String> = listOf("tag1", "tag2"),
        var upVotedList: List<String> = listOf("id1", "id2")
)