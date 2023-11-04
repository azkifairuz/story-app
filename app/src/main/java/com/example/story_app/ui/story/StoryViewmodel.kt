package com.example.story_app.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.StoryPagingSource
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.ListStoryItemWithMap
import com.example.story_app.data.repo.StoryRepository


class StoryViewModel(
    storyRepository: StoryRepository,
) : ViewModel() {

    private val apiService = ApiConfig.getApiService("your_token")

    val storyFlow = Pager(PagingConfig(pageSize = 20)) {
        StoryPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    val listStory: LiveData<PagingData<ListStoryItem>> by lazy {
        storyRepository.getStory().cachedIn(viewModelScope)
    }
    val listStoryWithMap: LiveData<List<ListStoryItemWithMap>> =
        storyRepository.getStoryWithLocation()
}




