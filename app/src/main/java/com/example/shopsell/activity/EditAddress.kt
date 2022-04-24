package com.example.shopsell.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Address
import com.example.shopsell.model.Constants
import com.myshoppal.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_edit_address.*

class EditAddress : BaseActivity() {
    private var maddressdetails:Address ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)
        setupActionBar()
        btn_submit_address.setOnClickListener {
           saveAddressToFirestore()
        }
        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            maddressdetails=intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)!!
        }
        if (maddressdetails != null) {
            if (maddressdetails!!.id.isNotEmpty()) {

                 tv_title_edit.text=resources.getString(R.string.edit)
                btn_submit_address.text = resources.getString(R.string.btn_lbl_update)

                et_full_name.setText(maddressdetails?.name)
                et_phone_number.setText(maddressdetails?.mobilenumber)
                et_address.setText(maddressdetails?.address)
                et_zip_code.setText(maddressdetails?.zipcode)


                when (maddressdetails?.type) {
                    Constants.HOME -> {
                        rb_home.isChecked = true
                    }
                    Constants.OFFICE -> {
                        rb_office.isChecked = true
                    }
                    else -> {
                        rb_other.isChecked = true

                    }
                }
            }
        }


    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_edit_address_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        }

        toolbar_add_edit_address_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(et_full_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }

            rb_other.isChecked && TextUtils.isEmpty(
                et_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun saveAddressToFirestore() {

        // Here we get the text from editText and trim the space
        val fullName: String = et_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = et_phone_number.text.toString().trim { it <= ' ' }
        val address: String = et_address.text.toString().trim { it <= ' ' }
        val zipCode: String = et_zip_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = et_additional_note.text.toString().trim { it <= ' ' }

        if (validateData()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            val addressType: String = when {
                rb_home.isChecked -> {
                    Constants.HOME
                }
                rb_office.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }


            val addressModel = Address(
                FirestoreClass().getcurrentuserid(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,

            )
            if (maddressdetails!=null&& maddressdetails!!.id.isNotEmpty()){
                FirestoreClass().updateAddress(this,addressModel,maddressdetails!!.id)
            }else {
                FirestoreClass().addAddress(this@EditAddress, addressModel)
            }

        }
    }

fun addUpdateAddressSuccess(){


        hideProgressDialog()

        showErrorSnackBar("Your Address Is Updated Successfully",false)
        finish()
    setResult(RESULT_OK)
    }


}
