package com.example.lottoapp

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class NumbDrawingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numb_drawing)
        val drawButton = findViewById<Button>(R.id.generateNbButton)
        val buttons = listOf(
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button2),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6)
        )
        val prizePotTextView = findViewById<TextView>(R.id.prizePotTextView)
        val winningsTextView = findViewById<TextView>(R.id.winningsTextView)
        val sharedPref = getSharedPreferences("lotto_app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        var prizePot: Float = sharedPref.getFloat("prizePot", 0.0f)
        prizePotTextView.text = prizePot.toString()
        val intent = intent
        var selectedNumbers = intent.getIntArrayExtra("SELECTEDNUMBERS")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 6

        val delayTime: Long = 1000

        drawButton.setOnClickListener {
            var winnings: Float = 0.0f
            drawButton.isEnabled=false
            prizePot += 100;
            prizePotTextView.text = prizePot.toString()
            var matchCounter = 0


            for (button in buttons) {
                button.visibility = View.INVISIBLE
            }
            GlobalScope.launch(Dispatchers.Main) {
                var generatedNumbers = drawNumbers()
                var progressStatus = 0
                var isMatch: Boolean = false;
                for (number in generatedNumbers){
                    var button = buttons[progressStatus]
                    if (selectedNumbers != null) isMatch = number in selectedNumbers
                    if (isMatch) matchCounter++
                    displayNumber(button, isMatch, number)
                    progressStatus++
                    progressBar.progress = progressStatus
                    delay(delayTime)
                }
                drawButton.isEnabled = true
                winnings = calculateWinnings(prizePot, matchCounter)
                prizePot -= winnings
                prizePotTextView.text = prizePot.toString()
                winningsTextView.text = winnings.toString()
                editor.putFloat("prizePot", prizePot)
                editor.apply()
            }
        }

    }
    private fun calculateWinnings(
        prizePot: Float,
        numbersMatchCount: Int
    ): Float {
        val winningRatios = mapOf(
            0 to 0.0f,
            1 to 0.01f,
            2 to 0.03f,
            3 to 0.05f,
            4 to 0.1f,
            5 to 0.3f,
            6 to 1.0f
        )
        return prizePot * winningRatios[numbersMatchCount]!!
    }

    private fun displayNumber(
        button: Button,
        isMatch: Boolean,
        number: Int
    ) {
        button.text = number.toString()
        if (isMatch) {
            button.setBackgroundColor(Color.GREEN)
            button.setTextColor(Color.WHITE)
        } else {
            button.setBackgroundColor(Color.RED)
            button.setTextColor(Color.WHITE)
        }

        button.visibility = View.VISIBLE
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