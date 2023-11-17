package com.example.lottoapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.random.Random
import com.example.lottoapp.databinding.ActivityNumbDrawingBinding
import com.google.firebase.firestore.FirebaseFirestore

class NumbDrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumbDrawingBinding
    private lateinit var db: FirebaseFirestore
    private val tag = "NumbDrawingActivity"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumbDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra("user", User::class.java)
        var selectedNumbers = intent.getIntArrayExtra("SELECTEDNUMBERS")

        db = FirebaseFirestore.getInstance()


        binding.progressBar.max = 6
        val delayTime: Long = 1000

        val buttonsList = listOf(
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6
        )

        binding.generateNbButton.setOnClickListener {

            var winnings: Double = 0.0
            binding.generateNbButton.isEnabled = false



            for (button in buttonsList)
                button.visibility = View.INVISIBLE

            var generatedNumbers = drawNumbers()
            var matches: Int =
                selectedNumbers?.toSet()?.intersect(generatedNumbers.toSet())?.count()!!

            GlobalScope.launch(Dispatchers.Main) {
                updateUIWithGeneratedNumbers(
                    generatedNumbers,
                    selectedNumbers,
                    binding.progressBar,
                    delayTime,
                    buttonsList
                )
                val winsNumb = simPlayers(doubleArrayOf(7.2e-8, 1.8e-5, 0.00097, 0.077))
                val win = calculateWin(50e6, winsNumb, matches)
                binding.winningsTextView.text = "Winnings: ${win.toString()}zł"
                if (user?.id != null){
                    val userRef = db.collection("users").document(user.id!!)
                    userRef.update("winnings", user.winnings + win)
                }
                binding.generateNbButton.isEnabled = true
            }
        }

    }
}





    private suspend fun updateUIWithGeneratedNumbers(generatedNumbers: IntArray, selectedNumbers: IntArray,
                                                     progressBar: ProgressBar, delayTime: Long, buttonsList: List<Button>) {
        var progressStatus = 0
        var isMatch: Boolean = false
        for (number in generatedNumbers) {
            var button = buttonsList[progressStatus]
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





