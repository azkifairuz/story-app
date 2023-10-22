package com.example.story_app.data

import com.example.story_app.data.response.ErrorResponse
import com.example.story_app.data.response.LoginResponse
import com.example.story_app.data.response.StoryResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ErrorResponse
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
    ): Call<StoryResponse>
}