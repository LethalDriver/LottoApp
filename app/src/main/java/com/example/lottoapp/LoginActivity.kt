package com.example.lottoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import com.example.lottoapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    private val tag = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()


        binding.loginButton.setOnClickListener {

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showSnackBar("Invalid email", true)
                return@setOnClickListener
            }


            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        val usersRef = db.collection("users")
                        val query = usersRef.whereEqualTo("email", email)

                        query.get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val user = document.toObject(User::class.java)
                                    user.id = document.id
                                    val intent = Intent(this@LoginActivity, NumbSelectActivity::class.java)
                                    intent.putExtra("user", user)
                                    startActivity(intent)
                                    Log.d(tag, "User: ${user.email} ${user.name}, winnings:  ${user.winnings}")
                                }

                            }
                            .addOnFailureListener { exception ->
                                Log.w(tag, "Error getting documents: ", exception)
                            }


                    } else {
                        Log.w(tag, "Error signing in: ", task.exception)
                        showSnackBar("Login failed", true)
                    }

                }

            }

    }

    fun registerLinkClicked(view: View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
}
