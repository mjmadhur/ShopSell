package com.example.shopsell.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Orders(
    val user_id: String = "",
    val items: ArrayList<CartProduct> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val orderstatus:String="pending",
    var id: String = ""

):Parcelable
