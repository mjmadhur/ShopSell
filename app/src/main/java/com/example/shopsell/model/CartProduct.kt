package com.example.shopsell.model

import android.icu.text.CaseMap
import android.os.Parcelable
import com.myshoppal.models.Product
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProduct(
    val user_id: String = "",
    val product_ownerid:String="",
    var product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    var cart_quantity:String="",
    var stock_quantity: String = "",
    var id:String=""


):Parcelable
