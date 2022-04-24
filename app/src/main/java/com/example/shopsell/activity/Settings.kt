package com.example.shopsell.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Constants
import com.example.shopsell.model.User
import com.google.firebase.auth.FirebaseAuth
import com.myshoppal.ui.activities.BaseActivity
import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : BaseActivity(), View.OnClickListener {
    private lateinit var muserdetails:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()
        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
        ll_address.setOnClickListener(this)
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getUserDetails(){
        showProgressDialog("Please Wait")
        FirestoreClass().getUserDetails(this)
    }
     fun userdetailsSuccess(user:User){
         muserdetails=user
        hideProgressDialog()
        GlideLoader(this).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstname} ${user.lastname}"
        tv_email.text=user.email
tv_mobile_number.text=user.mobile.toString()
    }

    override fun onResume() {

        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.btn_logout->{
                    FirebaseAuth.getInstance().signOut()
                    val intent=Intent(this,LogIn::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit->{
                    val intent=Intent(this,UserProfile::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,muserdetails)
                    startActivity(intent)
                }
                R.id.ll_address->{
                    startActivity(Intent(this,AddressList::class.java))
                }
            }
        }

    }
}