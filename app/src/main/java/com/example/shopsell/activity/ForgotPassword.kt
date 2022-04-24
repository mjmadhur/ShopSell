package com.example.shopsell.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shopsell.R
import com.google.firebase.auth.FirebaseAuth
import com.myshoppal.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupActionBar()
        btn_submit.setOnClickListener {
            val email=et_email.text.toString().trim { it <= ' '}
            if (email.isEmpty()){
                showErrorSnackBar("Please Enter A Email Address In Order To Reset Password",true)
            }else{
                showProgressDialog("Please Wait")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        task->
                        hideProgressDialog()
                        if (task.isSuccessful){
                            Toast.makeText(this,"Reset Password Email sent Successful",Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }

        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }
    }

}