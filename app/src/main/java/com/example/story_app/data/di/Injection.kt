package com.example.story_app.data.di

import android.content.Context
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.repo.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = AuthPreference(context)
        val user = pref.getUser()
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}