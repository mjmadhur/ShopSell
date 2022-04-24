package com.example.shopsell.activity.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsell.BaseFragment
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Orders
import com.myshoppal.ui.adapters.MyOrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment : BaseFragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       /* notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)*/


        val root = inflater.inflate(R.layout.fragment_orders, container, false)


        /*notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }
    fun populateorderslist(orderlist:ArrayList<Orders>){
        hideProgressDialog()
        if (orderlist.size > 0) {

            rv_my_order_items.visibility = View.VISIBLE
            tv_no_order.visibility = View.GONE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), orderlist)
            rv_my_order_items.adapter = myOrdersAdapter
        } else {
            rv_my_order_items.visibility = View.GONE
            tv_no_order.visibility = View.VISIBLE
        }

    }

fun getmyorderslist(){
    showProgressDialog("Please Wait for A Moment")
    FirestoreClass().getmyordrs(this)

}

    override fun onResume() {
        super.onResume()
    getmyorderslist()
    }
}