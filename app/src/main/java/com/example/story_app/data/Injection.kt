package com.example.story_app.data

import android.content.Context
import com.example.story_app.data.database.StoryDb
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.repo.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val userPreference = AuthPreference(context)
        val user = userPreference.getUser()
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository(apiService)
    }
}