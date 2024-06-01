package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem

object DataDummy {

    fun generateDummyGetStoriesResponse(): List<ListStoryItem> {
        val listStoryItem: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photo url $i",
                "created at $i",
                "user $i",
                "description $i",
                null,
                i.toString(),
                null,

            )
            listStoryItem.add(story)
        }
        return listStoryItem
    }
}