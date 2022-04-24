package com.myshoppal.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsell.R
import com.example.shopsell.activity.Productdetails
import com.example.shopsell.model.Constants

import com.myshoppal.models.Product
import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*


open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_dashboard_item_image
            )
            holder.itemView.tv_dashboard_item_title.text = model.title
            holder.itemView.tv_dashboard_item_price.text = "Rs.${model.price}"
holder.itemView.setOnClickListener{
    val intent=Intent(context,Productdetails::class.java)
    intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
    intent.putExtra(Constants.EXTRA_OWNER_ID,model.user_id)
    context.startActivity(intent)
}
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}