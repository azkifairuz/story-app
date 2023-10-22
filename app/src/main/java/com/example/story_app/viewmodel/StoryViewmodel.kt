package com.example.story_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.repo.StoryRepository
import com.example.story_app.data.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getStoryList() = liveData<ListStoryItem>(Dispatchers.IO) {
        _isLoading.value = true
        try {
            val storyResponse = storyRepository.getListStory()
            _isLoading.value = false

        } catch (exception: Exception) {
            _isLoading.value = false
        }
    }

}




