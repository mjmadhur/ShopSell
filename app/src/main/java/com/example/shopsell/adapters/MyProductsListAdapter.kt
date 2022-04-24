package com.myshoppal.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsell.R
import com.example.shopsell.activity.Addproducts
import com.example.shopsell.activity.Productdetails
import com.example.shopsell.activity.ui.home.MyProductsFragment
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Constants

import com.myshoppal.models.Product

import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class MyProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: MyProductsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
// END


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

            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "Rs.${model.price}"


            holder.itemView.ib_delete_product.setOnClickListener {


                fragment.deleteProduct(model.product_id)

                // END
            }
            if (model.stock_quantity.toInt()==0){
                holder.itemView.isClickable=false
            }
            holder.itemView.setOnClickListener{
                val intent=Intent(context,Productdetails::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                intent.putExtra(Constants.EXTRA_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
            holder.itemView.textViewstock.text=model.stock_quantity
holder.itemView.add_stock.setOnClickListener {

    fragment.updatestock(model.product_id,model)
}
            // END
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}