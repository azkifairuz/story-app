package com.example.story_app

import android.util.Log
import com.example.story_app.data.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "photourl + $i",
                null,
                "name + $i",
                "desc + $i",
                0.1,
                0.1
            )
            items.add(story)
        }
        return items
    }
}