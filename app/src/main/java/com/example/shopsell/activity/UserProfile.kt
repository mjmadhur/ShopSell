package com.example.shopsell.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shopsell.MainActivity
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Constants
import com.example.shopsell.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myshoppal.ui.activities.BaseActivity
import com.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_log_in.*

import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.et_email
import kotlinx.android.synthetic.main.activity_user_profile.tv_title
import java.io.IOException


class UserProfile : BaseActivity() , View.OnClickListener{
    private lateinit var mauth:FirebaseAuth
    private lateinit var muserdetails:User
    private var mselectedimageUri:Uri?=null
    private var muserimageUrl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

mauth=Firebase.auth
        val user=mauth.currentUser
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            muserdetails=intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        iv_user_photo.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        /*et_first_name.setText(user!!.displayName)
        et_last_name.setText(user!!.displayName)
        et_email.setText(user!!.email)
        et_mobile_number.setText(user.phoneNumber.toString())*/
        et_first_name.setText(muserdetails.firstname)
        et_last_name.setText(muserdetails.lastname)
        et_email.setText(muserdetails.email)
        et_email.isEnabled=false
        et_mobile_number.setText(muserdetails.mobile.toString())
if (muserdetails.profilecompleted==0){
    et_first_name.isEnabled=false

    et_last_name.isEnabled=false



    et_mobile_number.isEnabled=true



}else{
    et_first_name.isEnabled=true
    et_last_name.isEnabled=true
    setupActionBar()
    tv_title.text="Edit Profile"
    GlideLoader(this).loadUserPicture(muserdetails.image,iv_user_photo)
    if (muserdetails.mobile!=0L){
        et_mobile_number.setText(muserdetails.mobile.toString())
    }

}


    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
actionBar.title="Complete Profile"


        }
toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onClick(view: View?) {
     when(view!!.id){
         R.id.iv_user_photo->{
             if (ContextCompat.checkSelfPermission(
                     this,
                     Manifest.permission.READ_EXTERNAL_STORAGE
                 )
                 == PackageManager.PERMISSION_GRANTED
             ) {

Constants.showImageChooser(this)
             } else {

                 /*Requests permissions to be granted to this application. These permissions
                  must be requested in your manifest, they should not be granted to your app,
                  and they should have protection level*/

                 ActivityCompat.requestPermissions(
                     this,
                     arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                     Constants.READ_STORAGE_PERMISSION_CODE
                 )
             }
         }
         R.id.btn_submit->{

             if(validateUserProfileDetails()) {
                 showProgressDialog("Please Wait")
                 if (mselectedimageUri != null) {

                     FirestoreClass().uploadImageToCloudStorage(this, mselectedimageUri,Constants.USER_PROFILE_IMAGE)
                 } else {
                     val userhashmap = HashMap<String, Any>(

                     )
                     val mobile = et_mobile_number.text.toString().trim { it <= ' ' }
val firstname=et_first_name.text.toString().trim{it <= ' '}
                     if (firstname!=muserdetails.firstname){
                         userhashmap[Constants.FIRST_NAME]=firstname.toString()
                     }
                     val lastname=et_last_name.text.toString().trim{it <=' '}
                     if (lastname!=muserdetails.lastname){
                         userhashmap[Constants.LAST_NAME]=lastname.toString()
                     }
                     //showErrorSnackBar("Your Details Are Valid you Can Update Them",false)
                     if (mobile.isNotEmpty()&& mobile!=muserdetails.mobile.toString()) {
                         userhashmap[Constants.MOBILE_NUMBER] = mobile.toLong()
                     }
                     if (muserimageUrl.isNotEmpty()){
                         userhashmap[Constants.IMAGE]=muserimageUrl
                     }
                     if (muserdetails.profilecompleted == 0) {
                         userhashmap[Constants.PROFILE_COMPLETED] = 1
                     }


                     // showProgressDialog("Please Wait")
                     FirestoreClass().Updateuser(this, userhashmap)
                 }
             }
         }
     }

         }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
Constants.showImageChooser(this@UserProfile)
                //showErrorSnackBar("The storage permission is granted.", false)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mselectedimageUri= data.data!!



                        GlideLoader(this@UserProfile).loadUserPicture(
                            mselectedimageUri!!,
                            iv_user_photo
                        )
                        // END
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfile,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }
    private fun validateUserProfileDetails(): Boolean {
          if (muserimageUrl.isEmpty()){
            Toast.makeText(this,"Enter Imge",Toast.LENGTH_SHORT).show()
        }
        return when {


            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false

            }




            else -> {
                true
            }
        }
    }
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfile,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfile, Dashboard::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {

        //hideProgressDialog()
        muserimageUrl=imageURL
        val userhashmap = HashMap<String, Any>(

        )
        val mobile = et_mobile_number.text.toString().trim { it <= ' ' }

        //showErrorSnackBar("Your Details Are Valid you Can Update Them",false)
        if (mobile.isNotEmpty()) {
            userhashmap[Constants.MOBILE_NUMBER] = mobile.toLong()
        }
        if (muserimageUrl.isNotEmpty()){
            userhashmap[Constants.IMAGE]=muserimageUrl
        }

        userhashmap[Constants.PROFILE_COMPLETED]=1
        //showProgressDialog("Please Wait")
        FirestoreClass().Updateuser(this, userhashmap)
    }

}
