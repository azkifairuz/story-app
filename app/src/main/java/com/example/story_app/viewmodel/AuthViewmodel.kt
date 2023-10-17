package com.example.story_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.response.ErrorResponse
import kotlinx.coroutines.launch

class AuthViewmodel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse =  MutableLiveData<ErrorResponse>()
    val registerResponse: LiveData<ErrorResponse> get() = _registerResponse
    fun registerUser(name:String,email:String,password:String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().register(name, email, password)
                _registerResponse.value = ErrorResponse(
                    response.error,
                    response.message
                )
                _isLoading.value = false
            } catch (e: Exception) {
                e.message?.let {
                    ErrorResponse(
                        true,
                        it
                    )
                }
                _isLoading.value = false
            }
        }
    }
}