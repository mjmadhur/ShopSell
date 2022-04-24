package com.example.shopsell.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.content.ContextCompat
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.CartProduct
import com.example.shopsell.model.Constants
import com.myshoppal.models.Product
import com.myshoppal.ui.activities.BaseActivity
import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_productdetails.*
import kotlinx.android.synthetic.main.activity_register.*

class Productdetails : BaseActivity(),View.OnClickListener {
    private var mproductid:String=""
    private lateinit var mpdetails:Product
    private  var mproductowner:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdetails)
        setupActionBar()
        btn_add_to_cart.setOnClickListener {
            showErrorSnackBar("Adding Product Please Wait....",false)
        }
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mproductid=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("productid",mproductid)


}

      //  var prdouctowner:String=""
        if (intent.hasExtra(Constants.EXTRA_OWNER_ID)){
            mproductowner=intent.getStringExtra(Constants.EXTRA_OWNER_ID)!!
        }
        if (FirestoreClass().getcurrentuserid()==mproductowner){
            btn_add_to_cart.visibility= View.GONE
            btn_go.visibility=View.GONE
            showErrorSnackBar("You are The Owner of Product.So Uh Cannot buy It",true)
        }else{
            btn_add_to_cart.visibility=View.VISIBLE

        }
        btn_add_to_cart.setOnClickListener(this)
        btn_go.setOnClickListener(this)

        getProductDetailsfrag()
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }


        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
    fun productDetailsSuccess(product: Product) {
mpdetails=product
        // Hide Progress dialog.
        //hideProgressDialog()
        if(product.stock_quantity.toInt() == 0){

            // Hide Progress dialog.
            hideProgressDialog()

            // Hide the AddToCart button if the item is already in the cart.
            btn_add_to_cart.visibility = View.GONE

            tv_product_details_available_quantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            tv_product_details_available_quantity.setTextColor(
                ContextCompat.getColor(
                    this@Productdetails,
                    R.color.colorSnackBarError
                )
            )
        }
        // Populate the product details in the UI.
        GlideLoader(this@Productdetails).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text = "Rs.${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_available_quantity.text = product.stock_quantity
        if (FirestoreClass().getcurrentuserid()==product.user_id){
            hideProgressDialog()
        }else{
            FirestoreClass().checkIfItemExistInCart(this,mproductid)
        }
    }
    private fun getProductDetailsfrag() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getProductDetails(this@Productdetails, mproductid)
    }

    override fun onClick(p0: View?) {
if (p0!=null){
    when(p0.id){
        R.id.btn_add_to_cart->{
            addtocart()
        }
        R.id.btn_go->{
            startActivity(Intent(this,Cart::class.java))
        }
    }
}
    }



     fun addtocart(){
        val addedcart=CartProduct(
            FirestoreClass().getcurrentuserid(),
            mproductowner,
      mproductid,
            mpdetails.title,
            mpdetails.price,
            mpdetails.image,
            Constants.DEFAULT_CART_QUANTITY,


        )
         showProgressDialog("Please Wait")
         FirestoreClass().addcartitems(this,addedcart)
    }
    fun addcartsuccess(){
        hideProgressDialog()
        showErrorSnackBar("Succcessfully Added To Cart",false)
        btn_add_to_cart.visibility=View.GONE
        btn_go.visibility=View.VISIBLE
    }
    fun productExistsInCart() {


        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        btn_add_to_cart.visibility = View.GONE

        btn_go.visibility = View.VISIBLE
    }

}