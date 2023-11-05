package com.example.story_app.ui.story

import android.util.Log
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
import com.example.story_app.data.repo.StoryRepository
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    private var _listStory = MutableLiveData<List<ListStoryItem>>()
//    val listStory: LiveData<List<ListStoryItem>> = _listStory
    private var _listStoryMap = MutableLiveData<List<ListStoryItem>>()
    val listStoryMap: LiveData<List<ListStoryItem>> = _listStoryMap

    // Flow of PagingData
    private var _listStory = MutableLiveData<Flow<PagingData<ListStoryItem>>>()
    val listStory: LiveData<Flow<PagingData<ListStoryItem>>> = _listStory

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    companion object {
        const val PAGE_SIZE = 20
    }
    fun getListStory(token: String) {
        _isLoading.value = true
        Log.d("StoryViewModel", "Fetching data...")
        val client = ApiConfig.getApiService(token).getAllStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("StoryViewModel", "Data loaded successfully.")
                    _listStory.value = Pager(
                        config = PagingConfig(
                            pageSize = PAGE_SIZE,
                            enablePlaceholders = false
                        ),
                        pagingSourceFactory = { StoryPagingSource(ApiConfig.getApiService(token)) }
                    ).flow.cachedIn(viewModelScope)
                } else {
                    Log.e("StoryViewModel", "Failed to load data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("StoryViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    //    fun getListStory(token: String) {
//        _listStory.value = Pager(
//            config = PagingConfig(
//                pageSize = PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { StoryPagingSource(ApiConfig.getApiService(token)) }
//        ).flow.cachedIn(viewModelScope)
////        val client = ApiConfig.getApiService(token).getAllStories()
////        client.enqueue(object : Callback<StoryResponse> {
////            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
////                _isLoading.value = false
////                if (response.isSuccessful) {
////                    _listStory.value = response.body()?.listStory as List<ListStoryItem>?
////                } else {
////                    Log.e("isFailed Get Story", " ${response.message()}")
////                }
////            }
////
////            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
////                _isLoading.value = false
////                Log.e("onFailure Get Story", "onFailure: ${t.message.toString()}")
////            }
////
////        })
//    }
    fun getListStoryMap(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getAllStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStoryMap.value = response.body()?.listStory as List<ListStoryItem>?
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




