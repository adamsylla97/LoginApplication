package com.example.loginapplication.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.loginapplication.R
import com.example.loginapplication.db.LoginDatabase
import com.example.loginapplication.db.Subscription
import com.example.loginapplication.db.User
import com.example.loginapplication.db.UserAndSubscription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.OffsetDateTime

class AfterLoginActivity : AppCompatActivity() {
    private val sharedPreferencesName = "SHARED_PREFERENCES"
    private val ACTUAL_DATE = "actualDate"
    private val TOKEN = "token"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)

        val database = LoginDatabase.create(this)

        val sharedPresences = this.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val actualLoggedToken = sharedPresences.getString(TOKEN, "not_found") ?: "not_found"
        GlobalScope.launch(Dispatchers.IO) {
            val users = database.userAndSubscriptionDao().getUsersWithSubscription()
            Log.i("supertest123", users.toString())
            Log.i("supertest123", actualLoggedToken)
            val loggedUser: UserAndSubscription? = users.find { it.user.token == actualLoggedToken }
            Log.i("supertest123 logged", loggedUser.toString())
            withContext(Dispatchers.Main) {
                checkDate(loggedUser)
            }
        }
    }

    private fun checkDate(userAndSubscription: UserAndSubscription?) {
        val actualDate = OffsetDateTime.now()
        try{
            val userDate = userAndSubscription!!.subscription.ending
            Log.i("supertest123", userDate.toString())
            if(actualDate.isBefore(userDate)) {
                Toast.makeText(this, "Subscription is active", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Subscription is not active", Toast.LENGTH_LONG).show()
                finishAndRemoveUser(userAndSubscription)
            }
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e("user", "no sub")
            finish()
        }
    }

    private fun finishAndRemoveUser(userAndSubscription: UserAndSubscription) {
        val database = LoginDatabase.create(this)
        database.userAndSubscriptionDao().deleteSubscription(userAndSubscription.subscription)
        database.userAndSubscriptionDao().deleteUser(userAndSubscription.user)
        finish()
    }
}