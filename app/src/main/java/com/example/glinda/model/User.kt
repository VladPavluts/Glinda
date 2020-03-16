package com.example.glinda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
//parc//seria
@Parcelize
data class User (val name:String, val bio:String,val profilePicturePath: String?) : Parcelable {
    constructor(): this("","",null)
}