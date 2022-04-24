package com.example.shopsell.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.CartProduct
import com.example.shopsell.model.Constants
import com.myshoppal.models.Product
import com.myshoppal.ui.activities.BaseActivity
import com.myshoppal.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_productdetails.*

class Cart : BaseActivity() {
    private lateinit var mprodlist:ArrayList<Product>
    private lateinit var mcartitemlist:ArrayList<CartProduct>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setupActionBar()
        btn_checkout.setOnClickListener {
            val intent= Intent(this,AddressList::class.java)
            intent.putExtra(Constants.EXRA_ADDRESS_SELECT,true)
            startActivity(intent)
        }
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_Cart)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }


        toolbar_Cart.setNavigationOnClickListener { onBackPressed() }
    }
    fun successCartItemsList(cartList: ArrayList<CartProduct>) {

        // Hide progress dialog.
        hideProgressDialog()
for (product in mprodlist){
    for (cartitem in cartList){
        if (product.product_id==cartitem.product_id){
            cartitem.stock_quantity=product.stock_quantity
        if (product.stock_quantity.toInt()==0){
            cartitem.cart_quantity=product.stock_quantity
        }
        }

    }
}
        mcartitemlist=cartList

        if (mcartitemlist.size > 0) {

            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@Cart)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@Cart,mcartitemlist,true)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mcartitemlist) {
                val availablequant=item.stock_quantity.toInt()
                if (availablequant>0){
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }


            }

            tv_sub_total.text = "Rs.$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            tv_shipping_charge.text = "Rs.10"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                tv_total_amount.text = "Rs.$total"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }

    }
    private fun getCartItemsList() {

        // Show the progress dialog.
        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCartList(this@Cart)
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
    getprodlist()
    }

fun successproductlistfromFirestore(productlist:ArrayList<Product>){
    hideProgressDialog()
mprodlist=productlist
getCartItemsList()

}
    fun getprodlist(){
        showProgressDialog("Please Wait")
        FirestoreClass().getallprodlist(this)

    }
    fun itemRemovedSuccess() {

        hideProgressDialog()

       showErrorSnackBar("Item Removed Successfully From Cart",false)

        getCartItemsList()
    }
    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartItemsList()
    }

}