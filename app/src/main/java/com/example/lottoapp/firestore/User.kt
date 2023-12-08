package com.example.lottoapp.firestore

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class User(val id: String? = "", val name: String? = "", val email: String? = "",
                val registeredUser: Boolean = false)


