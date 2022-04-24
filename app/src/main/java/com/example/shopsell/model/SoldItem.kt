package com.example.shopsell.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldItem(
    val user_id:String="",
    val title: String = "",
    val price: String = "",
    val sold_quantity: String = "",
    val image: String = "",

    val order_id: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val address: Address = Address(),
    var id: String = "",

):Parcelable
