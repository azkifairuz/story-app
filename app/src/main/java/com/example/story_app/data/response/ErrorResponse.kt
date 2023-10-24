package com.example.story_app.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @field:SerializedName("error")
    val error: Boolean = true,

    @field:SerializedName("message")
    val message: String = ""
)
