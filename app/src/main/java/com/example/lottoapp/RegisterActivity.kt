package com.example.lottoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import com.example.lottoapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: FirebaseFirestore
    private val tag = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        binding.registerButton.setOnClickListener {


            val email = binding.editTextEmailRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()
            val name = binding.editTextNameRegister.text.toString()
            val passwordConfirmation = binding.editTextPasswordRepeatRegister.text.toString()

            if (!EMAIL_ADDRESS.matcher(email).matches()) {
                showSnackBar("Invalid email", true)
                return@setOnClickListener
            }

            if (password.length < 6) {
                showSnackBar("Password should be at least 6 characters long", true)
                return@setOnClickListener
            }

            if (passwordConfirmation != password){
                showSnackBar("Passwords do not match", true)
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        showSnackBar("User created successfully", false)

                        val user = User(name = name, email = email)

                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d(tag, "DocumentSnapshot added with ID: ${documentReference.id}")
                                user.id = documentReference.id
                                val intent = Intent(this@RegisterActivity, NumbSelectActivity::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Log.w(tag, "Error adding document", e)
                            }


                    } else {
                        Log.e(tag, "Registration failed: ${task.exception?.message}")
                        showSnackBar("Registration failed", true)
                    }

                }
        }

    }

    fun loginLinkClicked(view: View) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}