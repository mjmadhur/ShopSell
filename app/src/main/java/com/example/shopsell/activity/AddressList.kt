package com.example.shopsell.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Address
import com.example.shopsell.model.Constants
import com.myshoppal.ui.activities.BaseActivity
import com.myshoppal.ui.adapter.AddressListAdapter

import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_settings.*

class AddressList : BaseActivity() {
private var mselectaddress:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        setupActionBar()


if (intent.hasExtra(Constants.EXRA_ADDRESS_SELECT)){
    mselectaddress=intent.getBooleanExtra(Constants.EXRA_ADDRESS_SELECT,false)
}
        if (mselectaddress){
          tv_title_address.text="Select Address"
        }
tv_add_address.setOnClickListener {
  val intent=Intent(this,EditAddress::class.java)
    startActivityForResult(intent,Constants.ADD_ADDRESS_CODE)
}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== RESULT_OK){
            getaddresslist()
        }else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "To add the address.")
        }

    }

    override fun onResume() {
        super.onResume()
        getaddresslist()
    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_address_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }

        toolbar_address_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {


        hideProgressDialog()



        for (i in addressList) {

            Log.i("Name and Address ", "${i.name} ::  ${i.address}")
            if (addressList.size > 0) {

                rv_address_list.visibility = View.VISIBLE
                tv_no_address_found.visibility = View.GONE

                rv_address_list.layoutManager = LinearLayoutManager(this@AddressList)
                rv_address_list.setHasFixedSize(true)


                val addressAdapter = AddressListAdapter(this@AddressList, addressList,mselectaddress)

                rv_address_list.adapter = addressAdapter
                if (!mselectaddress) {
                    val editSwipeHandler = object : SwipeEdit(this) {
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                            val adapter = rv_address_list.adapter as AddressListAdapter
                            adapter.notifyEditItem(
                                this@AddressList,
                                viewHolder.adapterPosition
                            )
                            // END
                        }
                    }
                    val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                    editItemTouchHelper.attachToRecyclerView(rv_address_list)
                }

            } else {
                rv_address_list.visibility = View.GONE
                tv_no_address_found.visibility = View.VISIBLE
            }

        }
    }
    fun getaddresslist(){
        showProgressDialog("Please Wait")
        FirestoreClass().getAddressesList(this)
    }

}