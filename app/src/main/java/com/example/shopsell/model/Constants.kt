package com.example.shopsell.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val EXTRA_ORDER_DETAILS: String="extra_order_details"
    const val USERS:String="users"
    const val PRODUCTS:String="products"
    const val ORDERS:String="orders"
    const val MYSHOPPAL_PREFERENCES="MyShopPalPrefs"
    const val LOGGED_IN_USERNAME="logged_in_username"
    const val EXTRA_USER_DETAILS:String="extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE:Int=2
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val MOBILE_NUMBER:String="mobile"
    const val USER_PROFILE_IMAGE="User_Profile_Image"
    const val IMAGE="image"
    const val FIRST_NAME="firstname"
    const val LAST_NAME="lastname"
    const val USER_ID="user_id"
    const val EXTRA_PRODUCT_ID="extra_product_id"
    const val EXTRA_OWNER_ID:String="extra_owner_id"
const val PROFILE_COMPLETED:String="profilecompleted"
    const val PRODUCT_IMAGE:String="Product_Image"
    const val Cart_quantity="cart_quantity"
    const val CART_ITEMS:String="cart_items"
    const val PRODUCT_ID="product_id"
    const val HOME:String="home"
    const val OFFICE="office"
    const val OTHER="other"
    const val DEFAULT_CART_QUANTITY: String = "1"
const val STOCK_QUANTITY="stock_quantity"
    const val ADDRESS:String="address"
    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"
const val EXRA_ADDRESS_SELECT="Select_Address"
    const val ADD_ADDRESS_CODE:Int=121
    const val EXTRA_SELECTED_ADDRESS="extra_selected_Address"
    const val SOLD_ITEM="sold_item"

    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getfileExtension(activity: Activity,uri:Uri?):String?{
return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}