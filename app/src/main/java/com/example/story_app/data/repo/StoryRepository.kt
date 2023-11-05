package com.example.story_app.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.story_app.data.ApiService
import com.example.story_app.data.StoryPagingSource
import com.example.story_app.data.response.ListStoryItem

class StoryRepository(
    private val apiService: ApiService
) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

}