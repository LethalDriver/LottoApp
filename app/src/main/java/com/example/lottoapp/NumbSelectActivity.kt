package com.example.lottoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.lottoapp.databinding.ActivityNumbSelectBinding


class NumbSelectActivity : BaseActivity() {
    private lateinit var binding: ActivityNumbSelectBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumbSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra("user", User::class.java)

        binding.selectNumbersText.text = "${user?.name}, please select your Lucky numbers!"

        binding.numbersPicker.maxValue = 49
        binding.numbersPicker.minValue = 1

        binding.getRichButton.isEnabled = false

        val numbersArray = IntArray(6)
        var i = 0
        var text = ""

        binding.selectButton.setOnClickListener {
            val selectedNumber = binding.numbersPicker.value

            if (selectedNumber !in numbersArray){
                numbersArray[i++] = selectedNumber
                text += "$selectedNumber "
                binding.selectedNumbersView.text = text


                if (i > numbersArray.size - 1) {
                    binding.selectButton.isEnabled = false
                    binding.getRichButton.isEnabled = true
                }

                showSnackBar(
                    resources.getString(R.string.numbSelectedSuccessful),
                    error = false
                )

            } else showSnackBar(
                resources.getString(R.string.numbSelectedError),
                true
            )
        }

        binding.getRichButton.setOnClickListener{
            val intent = Intent(this, NumbDrawingActivity::class.java)
            intent.putExtra("SELECTEDNUMBERS", numbersArray)
            intent.putExtra("user", user)
            startActivity(intent)
        }

    }
}
