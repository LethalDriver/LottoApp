package com.example.lottoapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        snackbar.setTextColor(
            ContextCompat.getColor(
                this@BaseActivity,
                R.color.white
            )
        )
        if (errorMessage) {
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackBarError
                )
            )
        } else {
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackBarSuccessful
                )
            )
        }
        snackbar.show()
    }

}