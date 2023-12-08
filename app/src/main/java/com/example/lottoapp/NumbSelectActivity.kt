package com.example.lottoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lottoapp.databinding.ActivityNumbSelectBinding
import com.example.lottoapp.firestore.FireStoreData
import com.example.lottoapp.firestore.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class NumbSelectActivity : BaseActivity() {
    private lateinit var binding: ActivityNumbSelectBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNumbSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val tag = "NumbSelectActivity"

        val currentUserUid = auth.currentUser?.uid


        db.collection("users").document(currentUserUid!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject<User>()
                    binding.selectNumbersText.text = "${user?.name}, please select your Lucky numbers!"
                }
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "get failed with $exception")
            }



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
            val firebaseData = FireStoreData(
                email = auth.currentUser?.email,
                selNumb = numbersArray.toList()
            )
            currentUserUid?.let {
                db.collection("usersNumbers").document(currentUserUid)
                    .set(firebaseData)
                    .addOnSuccessListener { documentReference ->
                        // Handle success
                        println("Document added with ID: ${currentUserUid}")
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        println("Error adding document: $e")
                    }
            }
            startActivity(intent)
        }

    }
}
