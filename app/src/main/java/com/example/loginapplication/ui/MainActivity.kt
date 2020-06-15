package com.example.loginapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.loginapplication.Config
import com.example.loginapplication.R
import com.example.loginapplication.db.LoginDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.time.OffsetDateTime

class MainActivity : AppCompatActivity(), MainView {

    private val sharedPreferencesName = "SHARED_PREFERENCES"
    private val ACTUAL_DATE = "actualDate"
    private val TOKEN = "token"
    private var isDateChanged = false
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = LoginDatabase.create(this)
        viewModel = LoginViewModel(database, this)

        viewModel.informationToDisplay.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        checkIfDateIsTempered()
        if(!isDateChanged) {
            insertActualDate()
            loginToLastLoggedUser()
        }

        loginButton.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()
            viewModel.login(username, password)
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    override fun startNewActivity() {
        startActivity(Intent(this, AfterLoginActivity::class.java))
    }

    private fun loginToLastLoggedUser() {
        val sharedPresences = this.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val value = sharedPresences.getString(TOKEN, "not_found") ?: "not_found"
        if(value != "not_found"){
            viewModel.isLastLoggedInDatabase(value)
        }
    }

    override fun addLastLoggedUser() {
        val sharedPresences = this.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        with(sharedPresences.edit()) {
            putString(TOKEN, Config.token)
            commit()
        }
    }

    private fun insertActualDate() {
        val date = OffsetDateTime.now()
        val sharedPresences = this.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        with(sharedPresences.edit()) {
            putString(ACTUAL_DATE, date.toString())
            commit()
        }
    }

    private fun checkIfDateIsTempered() {
        val sharedPresences = this.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val value = sharedPresences.getString(ACTUAL_DATE, "not_found")
        if(value != "not_found") {
            val date = OffsetDateTime.parse(value)

            val actualDate = OffsetDateTime.now()
            if(date.isBefore(actualDate)) {
                isDateChanged = false
                Toast.makeText(this, "All is ok", Toast.LENGTH_LONG).show()
            } else {
                isDateChanged = true
                Toast.makeText(this, "Data has been changed", Toast.LENGTH_LONG).show()
            }
        }
    }
}
