package com.example.story_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.local.reduceFileImage
import com.example.story_app.data.response.ErrorResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadStoryViewmodel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _responseBody = MutableLiveData<ErrorResponse>()
    val responseBody: LiveData<ErrorResponse>
        get() = _responseBody

    fun postStory(token: String, file: File, description: String {
        viewModelScope.launch {
            _isLoading.value = true
            val myFile = reduceFileImage(file)
            val fileRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), myFile)
            try {
                val client = ApiConfig.getApiService().postStory(
                    token,
                    MultipartBody.Part.createFormData("photo", file.name, fileRequestBody),
                    RequestBody.create("text/plain".toMediaTypeOrNull(), description),
                    null,
                    null
                )
                if (client.error) {
                    _responseBody.value = ErrorResponse(
                        true,
                        client.message
                    )
                } else {
                    //success response
                    _responseBody.value = client
                }

            } catch (e: Exception) {
                _responseBody.value = ErrorResponse(
                    true,
                    e.message.toString()
                )
            } finally {
                _isLoading.value = false
            }

        }
    }
}