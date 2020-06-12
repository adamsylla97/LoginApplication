package com.example.loginapplication.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.loginapplication.R
import com.example.loginapplication.db.LoginDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.time.OffsetDateTime

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesName = "SHARED_PREFERENCES"
    private val ACTUAL_DATE = "actualDate"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIfDateIsTempered()
        insertActualDate()

        val database = LoginDatabase.create(this)
        val viewModel = LoginViewModel(database)

        viewModel.informationToDisplay.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        loginButton.setOnClickListener {
            viewModel.test()
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
                Toast.makeText(this, "All is ok", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Data has been changed", Toast.LENGTH_LONG).show()
            }
        }
    }
}
