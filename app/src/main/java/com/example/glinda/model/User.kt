package com.example.glinda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (val name:String,
                 val bio:String,
                 val profilePicturePath: String?,
                 val registrationTokens:MutableList<String>) : Parcelable {
    constructor(): this("","",null, mutableListOf())
}