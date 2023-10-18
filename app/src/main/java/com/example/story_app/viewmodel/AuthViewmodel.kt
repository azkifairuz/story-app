package com.example.story_app.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.story_app.data.ApiConfig
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.response.ErrorResponse
import com.example.story_app.data.response.LoginResponse
import com.example.story_app.data.response.LoginResult
import kotlinx.coroutines.launch

class AuthViewmodel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isLoadingLogin = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoadingLogin

    private val _registerResponse = MutableLiveData<ErrorResponse>()
    val registerResponse: LiveData<ErrorResponse> get() = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse
    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().register(name, email, password)
                _registerResponse.value = ErrorResponse(
                    response.error,
                    response.message
                )
            } catch (e: Exception) {
                e.message?.let {
                    ErrorResponse(
                        true,
                        it
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginUser(context: Context, email: String, password: String) {
        viewModelScope.launch {
            _isLoadingLogin.value = true
            try {
                val result = ApiConfig.getApiService().login(email, password)
                val loginResponse = LoginResponse(
                    result.loginResult,
                    result.error,
                    result.message
                )
                _loginResponse.value = loginResponse

                val saveLogin = AuthPreference(context)
                saveLogin.saveUser(loginResponse.loginResult)

            } catch (e: Exception) {
                _loginResponse.value = LoginResponse(
                    LoginResult(
                        "gagal",
                        "gagal",
                        "gagal"
                    ),
                    true,
                    e.message.toString()
                )
            } finally {
                _isLoadingLogin.value = false
            }
        }
    }
}