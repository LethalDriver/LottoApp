package com.example.lottoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val tag = "StatisticsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val recyclerView: RecyclerView = findViewById(R.id.statisticRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid

        val query = db.collection("userGames")
            .document(userId!!)
            .collection("games")

        query.get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<String>()
                for (document in documents) {
                    list.add(document.id)
                }
                val adapter = MyAdapter(list)
                recyclerView.adapter = adapter

                Log.d(tag, list.toString())
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents: ", exception)
            }


    }
}