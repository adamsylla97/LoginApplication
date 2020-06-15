package com.example.loginapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginapplication.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        webview.settings.javaScriptEnabled = true
        webview.loadUrl("https://www.google.com")

    }
}
