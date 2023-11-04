package com.example.story_app.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.story_app.StoryRemoteMediator
import com.example.story_app.data.ApiService
import com.example.story_app.data.database.StoryDb
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.ListStoryItemWithMap
import com.example.story_app.data.response.LoginResult

class StoryRepository(
    private val storyDatabase: StoryDb,
    private val apiService: ApiService,
    private val user: LoginResult,
    private val context: Context
) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, user, context),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoryWithLocation(): LiveData<List<ListStoryItemWithMap>> =
        storyDatabase.storyDao().getAllStoryWithMap()
}