package com.example.lottoapp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var id: String? = "", val name: String?, val email: String?, var winnings: Double = 0.0): Parcelable{
    constructor(): this("", "", "", 0.0)
}