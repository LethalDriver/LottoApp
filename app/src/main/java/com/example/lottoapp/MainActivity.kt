package com.example.lottoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.loginButton)

        val inputEmail = findViewById<EditText>(R.id.editTextEmail)
        val inputPassword = findViewById<EditText>(R.id.editTextPassword)
        loginButton.setOnClickListener {

            var email = inputEmail.text.toString()
            if (email.isEmpty()) {
                email = "unknown"
            }
            var password = inputPassword.text.toString()
            if (password.isEmpty()) {
                password = "unknown"
            }
            val intent = Intent(this, NumbSelectActivity::class.java)
            intent.putExtra("EMAIL", email)
            intent.putExtra("PASSWORD", password)
            startActivity (intent)
            }

    }
}
