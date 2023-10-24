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
        val switchView = findViewById<Switch>(R.id.ageSwitch)
        val buttonGame = findViewById<Button>(R.id.gameButton)
        buttonGame.isEnabled = false

        switchView.setOnCheckedChangeListener { _, isChecked ->
            buttonGame.isEnabled = isChecked
            if (isChecked){
                switchView.text = "YES"
            }
            else{
                switchView.text = "NO"
            }

        }
        val inputName = findViewById<EditText>(R.id.editTextName)
        val inputMail = findViewById<EditText>(R.id.editTextEmailAddress)
        val inputPhone = findViewById<EditText>(R.id.editTextPhone)
        buttonGame.setOnClickListener {

            var name = inputName.text.toString()
            if (name.isEmpty()) {
                name = "unknown"
            }
            var email = inputMail.text.toString()
            if (email.isEmpty()) {
                email = "unknown"
            }
            var phone = inputPhone.text.toString()
            if (phone.isEmpty()) {
                phone = "unknown"
            }
            val intent = Intent(this, NumbSelectActivity::class.java)
            intent.putExtra("NAME", name)
            intent.putExtra("EMAIL", email)
            intent.putExtra("PHONE", phone)
            startActivity (intent)
            }

    }
}
