package com.example.lottoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import kotlin.streams.toList

class NumbDrawingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numb_drawing)
        val intent = intent
        val displayNbTextView = findViewById<TextView>(R.id.displayNbTextView)
        var numbersString = intent.getIntArrayExtra("SELECTEDNUMBERS")?.joinToString(" ")
        displayNbTextView.text = numbersString
        val drawButton = findViewById<Button>(R.id.generateNbButton)
        val displayGeneratedNb = findViewById<TextView>(R.id.generatedNumbersTextView)
        drawButton.setOnClickListener {
            displayGeneratedNb.text = drawNumbers().joinToString(" ")
        }

    }

    private fun drawNumbers(): IntArray{
        val drawnNumbers: IntArray = IntArray(6)
        var i = 0
        while(i <= 5){
            var generatedNb = Random.nextInt(1, 49)
            if (generatedNb !in drawnNumbers){
                drawnNumbers[i] = generatedNb
                i++
            }
        }
        return drawnNumbers

    }
}