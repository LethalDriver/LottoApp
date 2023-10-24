package com.example.lottoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NumbDrawingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numb_drawing)
        val intent = intent
        val displayNbTextView = findViewById<TextView>(R.id.displayNbTextView)
        val numbers: IntArray? = intent.getIntArrayExtra("SELECTEDNUMBERS")
        var numbersString = ""
        if (numbers != null) {
            for (number in numbers){
                numbersString += "$number "
            }
        }
        displayNbTextView.text = numbersString
    }
}