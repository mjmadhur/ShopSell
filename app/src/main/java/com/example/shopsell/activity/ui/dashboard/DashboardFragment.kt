package com.example.shopsell.activity.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsell.BaseFragment
import com.example.shopsell.R
import com.example.shopsell.activity.Cart
import com.example.shopsell.activity.Settings

import com.example.shopsell.firestore.FirestoreClass
import com.myshoppal.models.Product
import com.myshoppal.ui.adapters.DashboardItemsListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import com.google.android.material.bottomnavigation.BottomNavigationView




class DashboardFragment : BaseFragment() {

   // private lateinit var dashboardViewModel: DashboardViewModel

private var mdetails:Product= Product()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onResume() {

        super.onResume()
        getdashboarditemlists()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        /*dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

inflater.inflate(R.menu.dashboard,menu)
        inflater.inflate(R.menu.cart,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.nav_settings->{
                startActivity(Intent(activity, Settings::class.java))
                return true
            }
            R.id.cart->{
                startActivity(Intent(activity,Cart::class.java))
                return true
            }


        }


        return super.onOptionsItemSelected(item)
    }
    fun successDashboardItemsList(dashboarditemslist:ArrayList<Product>){
        hideProgressDialog()

        if (dashboarditemslist.size > 0) {

            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 3)
            rv_dashboard_items.setHasFixedSize(true)


            val adapter = DashboardItemsListAdapter(requireActivity(), dashboarditemslist)
            rv_dashboard_items.adapter = adapter
        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }

    }
    private fun getdashboarditemlists(){
        showProgressDialog("Please Wait")

        FirestoreClass().getDashboardItemsList(this)

    }




    }

