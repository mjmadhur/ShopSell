package com.example.shopsell.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Address
import com.example.shopsell.model.CartProduct
import com.example.shopsell.model.Constants
import com.example.shopsell.model.Orders
import com.myshoppal.models.Product
import com.myshoppal.ui.activities.BaseActivity
import com.myshoppal.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_checkou.*

class Checkou : BaseActivity() {
    private var maddress:Address?=null
private lateinit var mprodlist:ArrayList<Product>
private lateinit var mcartlit:ArrayList<CartProduct>
private var msubtotal:Double=0.0
    private lateinit var morderdetails:Orders
    private var mTotalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkou)
        getlist()
        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            maddress=intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }
        btn_place_order.setOnClickListener {
            placeAnOrder()
        }

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        if (maddress!=null){
            tv_checkout_address_type.text=maddress!!.type
            tv_checkout_full_name.text=maddress!!.name
            tv_checkout_address.text="${maddress!!.address} ${maddress!!.zipcode}"
            tv_checkout_additional_note.text=maddress!!.Landmark
        }
        tv_mobile_number.text=maddress!!.mobilenumber
        setupActionBar()
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }


        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }
    fun successproductlistfromFirestore(productlist:ArrayList<Product>){
mprodlist=productlist
        getcartitemlist()
    }
    fun getlist(){
        showProgressDialog("Hey Wait For A Moment")
        FirestoreClass().getallprodlist(this)
    }
    fun getcartitemlist(){

        FirestoreClass().getCartList(this)
    }
    fun successcartlist(cartlist:ArrayList<CartProduct>){
        hideProgressDialog()
        mcartlit=cartlist
for (products in mprodlist){
    for (cartitems in mcartlit){
        if (products.product_id==cartitems.product_id){
            cartitems.stock_quantity=products.stock_quantity
        }
    }
}


        rv_cart_list_items.layoutManager=LinearLayoutManager(this)
        rv_cart_list_items.setHasFixedSize(true)
        val cartlistadapter=CartItemsListAdapter(this,mcartlit,false)
        rv_cart_list_items.adapter=cartlistadapter

        for (item in mcartlit) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                msubtotal += (price * quantity)
            }
        }

        tv_checkout_sub_total.text = "Rs.$msubtotal"
        // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
        tv_checkout_shipping_charge.text = "Rs.10.0"

        if (msubtotal> 0) {
            ll_checkout_place_order.visibility = View.VISIBLE

            mTotalAmount = msubtotal + 10.0
            tv_checkout_total_amount.text = "Rs.$mTotalAmount"
        } else {
            ll_checkout_place_order.visibility = View.GONE
        }


    }
    private fun placeAnOrder() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))


        morderdetails = Orders(
            FirestoreClass().getcurrentuserid(),
            mcartlit,
            maddress!!,
            "My order ${System.currentTimeMillis()}",
            mcartlit[0].image,
            msubtotal.toString(),
            "10.0", // The Shipping Charge is fixed as $10 for now in our case.
            mTotalAmount.toString(),

        )
        // END


        FirestoreClass().placeOrder(this@Checkou, morderdetails)
        // END
    }
    fun orderPlacedSuccess() {
FirestoreClass().updatedetailsatCheckout(this,mcartlit,morderdetails)

    }
    fun alldetailsupdatesuccess(){
        hideProgressDialog()

        Toast.makeText(this@Checkou, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@Checkou, Dashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }


}