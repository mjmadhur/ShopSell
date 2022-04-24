package com.example.shopsell.activity.ui.fragments.SoldProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopsell.BaseFragment
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.SoldItem
import com.myshoppal.ui.adapters.SoldProductsListAdapter
import kotlinx.android.synthetic.main.fragment_sold.*

/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"*/

/**
 * A simple [Fragment] subclass.
 * Use the [SoldFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoldFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

    override fun onResume() {
        super.onResume()
        getsoldlist()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold, container, false)
    }
    fun successSoldProductsList(soldlist:ArrayList<SoldItem>){
        hideProgressDialog()
        if (soldlist.size > 0) {
            rv_sold_product_items.visibility = View.VISIBLE
            tv_no_sold_products_found.visibility = View.GONE

            rv_sold_product_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(requireActivity(), soldlist)
            rv_sold_product_items.adapter = soldProductsListAdapter
        } else {
            rv_sold_product_items.visibility = View.GONE
            tv_no_sold_products_found.visibility = View.VISIBLE
        }
        // END
    }


    fun getsoldlist(){
        showProgressDialog("Please Wait")
        FirestoreClass().getSoldProductsList(this)
    }


}