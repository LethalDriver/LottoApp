package com.example.lottoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class NumbSelectActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.numb_layout)
        val intent = intent

        val name = intent.getStringExtra("NAME")
        val email = intent.getStringExtra("EMAIL")
        val phone = intent.getStringExtra("PHONE")

        val welcomeText = findViewById<TextView>(R.id.selectNumbersText)
        welcomeText.text = "$name, please select your Lucky numbers!"

        val numbersText = findViewById<TextView>(R.id.selectedNumbersView)
        val numbersPicker = findViewById<NumberPicker>(R.id.numbersPicker)

        numbersPicker.maxValue = 49
        numbersPicker.minValue = 1

        val selectButton = findViewById<Button>(R.id.selectButton)

        val getRichButton = findViewById<Button>(R.id.getRichButton)
        getRichButton.isEnabled = false

        val numbersArray = IntArray(6)
        var i = 0
        var text = ""

        selectButton.setOnClickListener {
            var selectedNumber = numbersPicker.value
            if (selectedNumber !in numbersArray){

                numbersArray[i++] = selectedNumber
                text += selectedNumber.toString()
                text = "$text "
                numbersText.text = text
                if (i > numbersArray.size - 1) {
                    selectButton.isEnabled = false
                    getRichButton.isEnabled = true
                }
                showErrorSnackBar(
                    resources.getString(R.string.numbSelectedSuccessful),
                    errorMessage = false
                )
            } else showErrorSnackBar(resources.getString(R.string.numbSelectedError), true)

        }
        getRichButton.setOnClickListener{
            val intent = Intent(this, NumbDrawingActivity::class.java)
            intent.putExtra("SELECTEDNUMBERS", numbersArray)
            startActivity(intent)
        }
    }
}
