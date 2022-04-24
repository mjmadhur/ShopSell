package com.example.shopsell.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.shopsell.activity.*
import com.example.shopsell.activity.ui.dashboard.DashboardFragment
import com.example.shopsell.activity.ui.fragments.SoldProducts.SoldFragment
import com.example.shopsell.activity.ui.home.MyProductsFragment
import com.example.shopsell.activity.ui.notifications.OrdersFragment
import com.example.shopsell.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firestore.v1.StructuredQuery
import com.myshoppal.models.Product

class FirestoreClass {
    private val firestore=FirebaseFirestore.getInstance()
    private lateinit var mauth:FirebaseAuth


    fun registerUser(activity: Register, userInfo: User) {


        firestore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)

            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }



    fun getcurrentuserid():String{
        val currentuser= FirebaseAuth.getInstance().currentUser
        var Currentuserid=""
        if (currentuser!=null){
            Currentuserid=currentuser.uid
        }
        return Currentuserid
    }
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        firestore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getcurrentuserid())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())


                val user = document.toObject(User::class.java)!!
val sharedpreferences=activity.getSharedPreferences(
    Constants.MYSHOPPAL_PREFERENCES,Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedpreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstname} ${user.lastname}"
                )
                editor.apply()




                when (activity) {
                    is LogIn-> {

                        activity.userLoggedInSuccess(user)
                    }
                    is Settings->{
                        activity.userdetailsSuccess(user)
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is LogIn -> {
                        activity.hideProgressDialog()
                    }
                    is Settings->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
    fun Updateuser(activity: Activity,userhashmap:HashMap<String ,Any>){
        firestore.collection(Constants.USERS).document(getcurrentuserid()).update(userhashmap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfile->{
                        activity.userProfileUpdateSuccess()
                    }


                }
            }
            .addOnFailureListener {
                e->
                when(activity){
                    is UserProfile->{
                        activity.hideProgressDialog()
                    }


                }
                Log.e(activity.javaClass.simpleName,"Failed",e)
            }
    }
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,imagetype:String) {


        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getfileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())


                        when (activity) {
                            is UserProfile -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is Addproducts->{
                                activity.imageUploadSucccess(uri.toString())
                            }
                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfile -> {
                        activity.hideProgressDialog()
                    }
                    is Addproducts->{
                        activity.hideProgressDialog()

                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    fun uploadProductDetails(activity: Addproducts, productInfo: Product) {

        firestore.collection(Constants.PRODUCTS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }
    fun getProductsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        firestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getcurrentuserid())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (fragment) {
                    is MyProductsFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is MyProductsFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }
    fun getDashboardItemsList(fragment: DashboardFragment) {
        // The collection name for PRODUCTS
        firestore.collection(Constants.PRODUCTS)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                    if (product.stock_quantity.toInt()==0){
                        firestore.collection(Constants.PRODUCTS).document(product.product_id).delete()
                    }
                }

                // Pass the success result to the base fragment.
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error which getting the dashboard items list.
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }
    fun deleteProduct(fragment: Fragment, productId: String) {
        firestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {

when(fragment ){
    is MyProductsFragment->{
                fragment.productDeleteSuccess()}

}

                // END
            }
            .addOnFailureListener { e ->

                when(fragment){
                    is MyProductsFragment->{
                        fragment.hideProgressDialog()
                    }
                }


                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    fun getProductDetails(activity: Productdetails, productId: String) {

        // The collection name for PRODUCTS
        firestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->


                Log.e(activity.javaClass.simpleName, document.toString())


                val product = document.toObject(Product::class.java)!!

if (product.stock_quantity.toInt()==0){
    firestore.collection(Constants.PRODUCTS).document(productId).delete()
}

                activity.productDetailsSuccess(product)
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }


fun addcartitems(activity: Productdetails, addtocart: CartProduct){
    firestore.collection(Constants.CART_ITEMS).document()
        .set(addtocart, SetOptions.merge())
        .addOnSuccessListener {
activity.addcartsuccess()
        }.addOnFailureListener {
            e->
            activity.hideProgressDialog()
            Log.e("Cart","Failed TO show Cart")
        }

}
    fun checkIfItemExistInCart(activity: Productdetails, productId: String) {

        firestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getcurrentuserid())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.documents.toString())


                // If the document size is greater than 1 it means the product is already added to the cart.
                if (document.documents.size > 0) {
                    activity.productExistsInCart()
                } else {
                    activity.hideProgressDialog()
                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }
    fun getCartList(activity: Activity) {
        // The collection name for PRODUCTS
        firestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getcurrentuserid())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of cart items in the form of documents.
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Cart Items ArrayList.
                val list: ArrayList<CartProduct> = ArrayList()

                // A for loop as per the list of documents to convert them into Cart Items ArrayList.
                for (i in document.documents) {

                    val cartItem = i.toObject(CartProduct::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }


                when (activity) {
                    is Cart -> {
                        activity.successCartItemsList(list)
                    }
                    is Checkou->{
                        activity.successcartlist(list)
                    }
                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error based on the activity instance.
                when (activity) {
                    is Cart -> {
                        activity.hideProgressDialog()
                    }
                    is Checkou->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "Error while getting the cart list items.", e)
            }
    }
fun getallprodlist(activity:Activity){
    firestore.collection(Constants.PRODUCTS).get()
        .addOnSuccessListener {
            document->
            Log.e("Prodlist", document.documents.toString())
            val productslist:ArrayList<Product> =ArrayList()
            for (i in document.documents){
                val product=i.toObject(Product::class.java)
                product!!.product_id=i.id
                productslist.add(product)
            }
            when(activity ) {
                is Cart -> {
                    activity.successproductlistfromFirestore(productslist)
                }
                is Checkou->{
                    activity.successproductlistfromFirestore(productslist)
                }
            }
        }.addOnFailureListener {
            when(activity){
                is Cart->{
                    activity.hideProgressDialog()
                }
            }

        }
}
    fun removeItemFromCart(context: Context, cart_id: String) {

        // Cart items collection name
        firestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {

                // Notify the success result of the removed cart item from the list to the base class.
                when (context) {
                    is Cart -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (context) {
                    is Cart -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from the cart list.",
                    e
                )
            }
    }
    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {

        // Cart items collection name
        firestore.collection(Constants.CART_ITEMS)
            .document(cart_id) // cart id
            .update(itemHashMap) // A HashMap of fields which are to be updated.
            .addOnSuccessListener {


                when (context) {
                    is Cart -> {
                        context.itemUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (context) {
                    is Cart ->
                        context.hideProgressDialog()
                }


                Log.e(
                    context.javaClass.simpleName,
                    "Error while updating the cart item.",

                    )
            }
    }
    fun updatemystock(fragment: Fragment,productid:String,itemHashMap: HashMap<String, Any>){
        firestore.collection(Constants.PRODUCTS).document(productid).update(itemHashMap).addOnSuccessListener {
            when(fragment){
                is MyProductsFragment->{
                    fragment.itemsuccess()
                }
            }
        }
    }
    fun addAddress(activity: EditAddress, addressInfo: Address) {

        // Collection name address.
        firestore.collection(Constants.ADDRESS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.addUpdateAddressSuccess()
                // END
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the address.",
                    e
                )
            }
    }
    fun getAddressesList(activity:AddressList) {
        firestore.collection(Constants.ADDRESS)
            .whereEqualTo(Constants.USER_ID, getcurrentuserid())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.documents.toString())

                val addressList: ArrayList<Address> = ArrayList()


                for (i in document.documents) {

                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id

                    addressList.add(address)
                }


                activity.successAddressListFromFirestore(addressList)
                // END
            }
            .addOnFailureListener { e ->


                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the address list.", e)
            }

    }
    fun updateAddress(activity: EditAddress, addressInfo: Address, addressId: String) {

        firestore.collection(Constants.ADDRESS)
            .document(addressId)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the Address.",
                    e
                )
            }
    }

    fun placeOrder(activity: Checkou, order: Orders) {

        firestore.collection(Constants.ORDERS)
            .document()

            .set(order, SetOptions.merge())
            .addOnSuccessListener {


                activity.orderPlacedSuccess()
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing an order.",
                    e
                )
            }
    }
    fun updatedetailsatCheckout(activity: Checkou,cartlist:ArrayList<CartProduct>,order:Orders){
        val writebatch=firestore.batch()
        for (cartitems in cartlist){

/*val prodHashmap=HashMap< String, Any>()
            prodHashmap[Constants.STOCK_QUANTITY]=(cartitems.stock_quantity.toInt()-cartitems.cart_quantity.toInt())
                .toString()*/
            val soldProduct = SoldItem(
                // Here the user id will be of product owner.
              cartitems.product_ownerid,
                cartitems.title,
                cartitems.price,
                cartitems.cart_quantity,
                cartitems.image,
                order.title,

                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val docref=firestore.collection(Constants.SOLD_ITEM).document(cartitems.product_id)
            writebatch.set(docref,soldProduct)

                }
        for (cart in cartlist) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.STOCK_QUANTITY] =
                (cart.stock_quantity.toInt() - cart.cart_quantity.toInt()).toString()

            val documentReference = firestore.collection(Constants.PRODUCTS)
                .document(cart.product_id)

            writebatch.update(documentReference, productHashMap)
        }

        for (cartitems in cartlist){
            val docref=firestore.collection(Constants.CART_ITEMS).document(cartitems.id)
            writebatch.delete(docref)
        }
        writebatch.commit().addOnSuccessListener {
activity.alldetailsupdatesuccess()
        }.addOnFailureListener {
            activity.hideProgressDialog()
        }

    }
fun getmyordrs(fragment: OrdersFragment){
    firestore.collection(Constants.ORDERS).whereEqualTo(Constants.USER_ID,
        getcurrentuserid()).get().addOnSuccessListener {
document->
val list:ArrayList<Orders> = ArrayList()
        for (i in document.documents){
            val orderplaced=i.toObject(Orders::class.java)!!
            orderplaced.id=i.id
            list.add(orderplaced)
        }
        fragment.populateorderslist(list)
    }.addOnFailureListener {
        fragment.hideProgressDialog()
    }
}
    fun getSoldProductsList(fragment: SoldFragment) {
        // The collection name for SOLD PRODUCTS
        firestore.collection(Constants.SOLD_ITEM)
            .whereEqualTo(Constants.USER_ID, getcurrentuserid())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of sold products in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Sold Products ArrayList.
                val list: ArrayList<SoldItem> = ArrayList()

                // A for loop as per the list of documents to convert them into Sold Products ArrayList.
                for (i in document.documents) {

                    val soldProduct = i.toObject(SoldItem::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }


                fragment.successSoldProductsList(list)
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error.
                fragment.hideProgressDialog()

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting the list of sold products.",
                    e
                )
            }
    }

}




