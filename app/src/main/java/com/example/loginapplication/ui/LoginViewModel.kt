package com.example.loginapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapplication.Config
import com.example.loginapplication.db.LoginDatabase
import com.example.loginapplication.db.Subscription
import com.example.loginapplication.db.User
import com.example.loginapplication.db.UserAndSubscription
import com.example.loginapplication.rest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class LoginViewModel(private val database: LoginDatabase, private val mainView: MainView) : ViewModel() {

    val informationToDisplay = MutableLiveData<String>()

    private val rest =
        RestServiceBuilder.build(RestLoginService::class.java)

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
//            val date = OffsetDateTime.now()
//            val user = User("token", date)
//            val insertedId = database.userAndSubscriptionDao().insertUser(user)
//            val subscription = Subscription(insertedId.toInt(), OffsetDateTime.now(), OffsetDateTime.of(2020, 12, 23, 20, 41, 20, 1, ZoneOffset.UTC))
//            database.userAndSubscriptionDao().insertSubscription(subscription)
//
//            val value = database.userAndSubscriptionDao().getUsersWithSubscription()
//            Log.i("supertest123", value.toString())
            try{
                val a = checkSubscription()
                Config.token = "aaa"
                val id = database.userAndSubscriptionDao().insertUser(User(Config.token, OffsetDateTime.now()))
                database.userAndSubscriptionDao().insertSubscription(Subscription(id.toInt(), OffsetDateTime.parse(a.start), OffsetDateTime.parse(a.ending)))
                mainView.addLastLoggedUser()
                mainView.startNewActivity()
                Log.i("supertest123", a.toString())
            } catch (e: Exception) {
                informationToDisplay.postValue("Login denied")
            }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try{
            //login
            val loginData = LoginData(email, password)
            val loginResponse = rest.testLogin()
            Log.i("supertest123", loginResponse.toString())
            Config.token = loginResponse.token
            val subscriptionResponse = checkSubscription()
            //insert to database
            val insertedId = database.userAndSubscriptionDao().insertUser(User(Config.token, OffsetDateTime.now()))
            database.userAndSubscriptionDao().insertSubscription(Subscription(insertedId.toInt(), OffsetDateTime.parse(subscriptionResponse.start), OffsetDateTime.parse(subscriptionResponse.ending)))
            mainView.addLastLoggedUser()
            mainView.startNewActivity()
        } catch (e: Exception) {
            e.printStackTrace()
            informationToDisplay.postValue("Login denied")
        }
    }

    fun isLastLoggedInDatabase(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val users = database.userAndSubscriptionDao().getUsersWithSubscription()
            if(users.any { it.user.token == token }) {
                mainView.startNewActivity()
            } else {
                Log.i("supertest123 is", "last logged is not in db")
            }
        }
    }

    fun checkIfSubscriptionIsActive() {

    }

    fun checkIfDateWasntChanged() {

    }

    private suspend fun checkSubscription() = withContext(Dispatchers.IO) {
        val subscriptions = rest.testGetSubscriptions()
        val newest = getNewest(subscriptions)
        Log.i("supertest123", newest.toString())
        newest
    }

    private fun getNewest(subscriptions: SubscriptionResponse): HydraMember {
        val members = subscriptions.hydra_member
        val filteredActive = members.filter { it.confirmed }
        var newest = filteredActive[0]
        filteredActive.forEach {
            val newestEnding = OffsetDateTime.parse(newest.ending)
            val actualEnding = OffsetDateTime.parse(it.ending)
            if(newestEnding.isBefore(actualEnding)) {
                newest = it
            }
        }
        return newest
    }
}