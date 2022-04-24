package com.myshoppal.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsell.R
import com.example.shopsell.activity.orderdetails
import com.example.shopsell.model.CartProduct
import com.example.shopsell.model.Constants
import com.example.shopsell.model.Orders

import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

// TODO Step 10: Create an adapter class for my list of orders.
// START
open class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Orders>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
var mcart:CartProduct=CartProduct()
        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_item_image
            )
holder.itemView.add_stock.visibility=View.GONE
            holder.itemView.mjj.text="Cart quantity"
            holder.itemView.textViewstock.text=mcart.cart_quantity
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "Rs.${model.total_amount}"

            holder.itemView.ib_delete_product.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val intent = Intent(context, orderdetails::class.java)
                intent.putExtra(Constants.EXTRA_ORDER_DETAILS, model)
                context.startActivity(intent)
            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
// END