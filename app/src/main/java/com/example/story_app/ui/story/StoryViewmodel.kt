package com.example.story_app.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.StoryPagingSource
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryViewModel : ViewModel() {

    private val apiService = ApiConfig.getApiService("your_token")

    val storyFlow = Pager(PagingConfig(pageSize = 20)) {
        StoryPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    fun getListStory(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getAllStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStory.value = response.body()?.listStory as List<ListStoryItem>?
                    Log.d("story", "onResponse: ${_listStory.value}")
                } else {
                    Log.e("isFailed Get Story", " ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure Get Story", "onFailure: ${t.message.toString()}")
            }

        })
    }
}




