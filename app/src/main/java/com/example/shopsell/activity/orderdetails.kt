package com.example.shopsell.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsell.R
import com.example.shopsell.model.Constants
import com.example.shopsell.model.Orders
import com.myshoppal.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_orderdetails.*

class orderdetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderdetails)
        setupActionBar()
        var orderdetails:Orders=Orders()
        if (intent.hasExtra(Constants.EXTRA_ORDER_DETAILS)){
            orderdetails=intent.getParcelableExtra(Constants.EXTRA_ORDER_DETAILS)!!
        }
        setupUI(orderdetails)
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_order_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }


        toolbar_my_order_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun setupUI(orderDetails: Orders) {

        tv_order_details_id.text = orderDetails.title


        // END

        rv_my_order_items_list.layoutManager = LinearLayoutManager(this)
        rv_my_order_items_list.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this, orderDetails.items, false)
        rv_my_order_items_list.adapter = cartListAdapter
tv_order_status.text=orderDetails.orderstatus
        tv_my_order_details_address_type.text = orderDetails.address.type
        tv_my_order_details_full_name.text = orderDetails.address.name
        tv_my_order_details_address.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipcode}"
        tv_my_order_details_additional_note.text = orderDetails.address.Landmark


        tv_my_order_details_mobile_number.text = orderDetails.address.mobilenumber

        tv_order_details_sub_total.text = "Rs.${orderDetails.sub_total_amount}"
        tv_order_details_shipping_charge.text = "Rs.${orderDetails.shipping_charge}"
        tv_order_details_total_amount.text = "Rs.${orderDetails.total_amount}"
    }
}