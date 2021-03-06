package com.myshoppal.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsell.R
import com.example.shopsell.model.SoldItem

import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

// TODO Step 6: Create an adapter class for Sold Products list.
// START
open class SoldProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<SoldItem>
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

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_item_image
            )
holder.itemView.add_stock.visibility=View.GONE
            holder.itemView.mjj.visibility=View.GONE
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "$${model.price}"

            holder.itemView.ib_delete_product.visibility = View.GONE
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
// END