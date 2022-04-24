package com.example.shopsell.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val id:String="",
    val firstname:String="",
    val lastname:String="",
    val email:String="",
    val image:String="",
    val mobile:Long=0,
    val profilecompleted:Int=0

):Parcelable
