package com.example.story_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.response.DetailStoryResponse
import com.example.story_app.data.response.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewmodel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _detailStory = MutableLiveData<Story?>()
    val detailStory: LiveData<Story?> = _detailStory

    fun getDetailStory(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).detailStory(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailStory.value = response.body()?.story
                } else {
                    Log.e("isFailed Get Detail", " ${response.body()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("isFailed Get Detail", " ${t.message.toString()}")
            }
        })
    }
}