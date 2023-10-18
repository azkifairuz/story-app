package com.example.story_app.data.repo

import androidx.lifecycle.LiveData
import com.example.story_app.data.ApiService
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.response.ErrorResponse
import com.example.story_app.data.response.StoryResponse


class StoryRepository  private constructor(
    private val apiService: ApiService,
    private val preference: AuthPreference
) {
    suspend fun getListStory() : StoryResponse {
        val user = preference.getUser()
        return apiService.getAllStories("Bearer ${user.token}")
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, preference: AuthPreference): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, preference).also {
                    instance = it
                }
            }
        }
    }
}
