package com.example.loginapplication.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapplication.rest.LoginData
import com.example.loginapplication.rest.LoginResponse
import com.example.loginapplication.rest.RestLoginService
import com.example.loginapplication.rest.RestServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    val informationToDisplay = MutableLiveData<String>().apply {
        value = ""
    }

    private val rest =
        RestServiceBuilder.build(RestLoginService::class.java)

    fun login(email: String, password: String) = viewModelScope.launch {
        val loginData = LoginData(email, password)
        val loginResponse = rest.login(loginData)
        checkSubscription(loginResponse)
    }

    private fun checkSubscription(loginResponse: LoginResponse) = viewModelScope.launch {


        informationToDisplay.value = "information to display"
    }

    private suspend fun checkSubscrption() = withContext(Dispatchers.IO) {

    }

}