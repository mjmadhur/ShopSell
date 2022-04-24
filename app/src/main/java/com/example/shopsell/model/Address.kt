package com.example.shopsell.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val user_id:String="",
    val name:String="",
    val mobilenumber:String="",
    val address:String="",
    val zipcode:String="",
    val Landmark:String="",
    var type:String="",
    var id:String=""
):Parcelable
