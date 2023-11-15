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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round
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
        val winningsTextView = findViewById<TextView>(R.id.winningsTextView)
        val sharedPref = getSharedPreferences("lotto_app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val intent = intent
        var selectedNumbers = intent.getIntArrayExtra("SELECTEDNUMBERS")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 6
        val delayTime: Long = 1000

        drawButton.setOnClickListener {
            var winnings: Float = 0.0f
            drawButton.isEnabled=false
            for (button in buttons) {
                button.visibility = View.INVISIBLE
            }
            var generatedNumbers = drawNumbers()
            var matches: Int =
                selectedNumbers?.toSet()?.intersect(generatedNumbers.toSet())?.count()!!

            GlobalScope.launch(Dispatchers.Main) {
                updateUIWithGeneratedNumbers(generatedNumbers, buttons, selectedNumbers, progressBar, delayTime)

            }

            GlobalScope.launch(Dispatchers.Main) {
                val winDeferred = async(Dispatchers.Default) {
                    val winsNumb = simPlayers(doubleArrayOf(7.2e-8, 1.8e-5, 0.00097, 0.077))
                    calculateWin(50e6, winsNumb, matches)
                }
                winningsTextView.text = winDeferred.await().toString()
                drawButton.isEnabled = true
            }
        }

    }

    private suspend fun updateUIWithGeneratedNumbers(generatedNumbers: IntArray, buttons: List<Button>, selectedNumbers: IntArray,
                                                     progressBar: ProgressBar, delayTime: Long) {
        var progressStatus = 0
        var isMatch: Boolean = false
        for (number in generatedNumbers) {
            var button = buttons[progressStatus]
            if (selectedNumbers != null) isMatch = number in selectedNumbers
            displayNumber(button, isMatch, number)
            progressStatus++
            progressBar.progress = progressStatus
            delay(delayTime)
        }

    }
    private fun calculateWin(cummulate: Double=0.0,
                             winners: IntArray
                             , score: Int = 0): Double {

        return when (score) {
            6 -> (cummulate * 0.44) / (winners[0] + 1)
            5 -> (cummulate * 0.08) / winners[1] + 1
            4 -> (cummulate * 0.48) / winners[2] + 1
            3 -> (cummulate * 0.48) / winners[3] + 1
            else -> 0.0
        }
    }

    private fun simPlayers(probabilityArray: DoubleArray, numberOfPopulation: Int=38000000): IntArray {
        val numberOfPlayers = Random.nextInt(numberOfPopulation)
        val numberOfWins = IntArray(4)
        for ((iterator, probability) in probabilityArray.withIndex()) {
            var numberOfWinningPlayers = round(probability * numberOfPlayers).toInt()
            numberOfWins[iterator] = numberOfWinningPlayers
        }
        return numberOfWins
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


