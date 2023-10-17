package com.example.story_app.data.repo

import com.example.story_app.data.ApiService
import com.example.story_app.data.response.RegisterResponse
import retrofit2.Call

class AuthRepository(private val apiService: ApiService) {
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Call<RegisterResponse> {
        return apiService.register(name, email, password)
    }
}