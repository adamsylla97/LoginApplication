package com.example.loginapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapplication.Config
import com.example.loginapplication.db.LoginDatabase
import com.example.loginapplication.db.Subscription
import com.example.loginapplication.db.User
import com.example.loginapplication.rest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class LoginViewModel(private val database: LoginDatabase) : ViewModel() {

    val informationToDisplay = MutableLiveData<String>().apply {
        value = ""
    }

    private val rest =
        RestServiceBuilder.build(RestLoginService::class.java)

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            val date = OffsetDateTime.now()
            val user = User("token", date)
            val insertedId = database.userAndSubscriptionDao().insertUser(user)
            val subscription = Subscription(insertedId.toInt(), OffsetDateTime.now(), OffsetDateTime.of(2020, 12, 23, 20, 41, 20, 1, ZoneOffset.UTC))
            database.userAndSubscriptionDao().insertSubscription(subscription)

            val value = database.userAndSubscriptionDao().getUsersWithSubscription()
            Log.i("supertest123", value.toString())
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        //login
        val loginData = LoginData(email, password)
        val loginResponse = rest.login(loginData)
        Config.token = loginResponse.token
        val subscriptionResponse = checkSubscription(loginResponse)
        //insert to database
        val insertedId = database.userAndSubscriptionDao().insertUser(User(Config.token, OffsetDateTime.now()))
        database.userAndSubscriptionDao().insertSubscription(Subscription(insertedId.toInt(), subscriptionResponse.start, subscriptionResponse.ending))
        //check if date wasnt changed

        //check if subscrption is active
    }

    fun checkIfSubscriptionIsActive() {

    }

    fun checkIfDateWasntChanged() {

    }

    private suspend fun checkSubscription(loginResponse: LoginResponse) = withContext(Dispatchers.IO) {
        val subscriptions = rest.getSubscriptions()
        val newest = getNewest(subscriptions)
        return@withContext newest
    }

    private fun getNewest(subscriptions: List<SubscriptionResponse>): HydraMember {
        return subscriptions[0].hydra_member[0]
    }
}