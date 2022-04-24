package com.example.shopsell.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.shopsell.MainActivity
import com.example.shopsell.R
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.Constants
import com.example.shopsell.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myshoppal.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_log_in.et_email
import kotlinx.android.synthetic.main.activity_log_in.et_password
import kotlinx.android.synthetic.main.activity_register.*

class LogIn :BaseActivity(),View.OnClickListener {
    private lateinit var mauth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
private var muser:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        @Suppress("Deprecation")
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_FULLSCREEN
tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        mauth=Firebase.auth
        val Currentuserid=FirestoreClass().getcurrentuserid()
       /* if (Currentuserid.isNotEmpty()  ) {
            // Start the Main Activity
            startActivity(Intent(this, Dashboard::class.java))
        }*/



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        btn_gsignin.setOnClickListener {
            signIn()
        }

    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val ecxception=task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("LogIn", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LogIn", "Google sign in failed", e)
                }
            }else{
                Log.w("Register","Failed")
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val user = mauth.currentUser
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mauth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LogIn", "signInWithCredential:success")
                    //
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()

                        Toast.makeText(this,"Hello ${user!!.displayName}",Toast.LENGTH_LONG).show()

                } else {

                    // If sign in fails, display a message to the user.
                    Log.w("LogIn", "signInWithCredential:failure", task.exception)

                }
            }
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.tv_forgot_password->{
startActivity(Intent(this,ForgotPassword::class.java))
                }
                R.id.btn_login->{
                    logInRegisteredUser()
                }
                R.id.tv_register->{
                    startActivity(Intent(this,Register::class.java))
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }


    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog("Please Wait")


            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog


                    if (task.isSuccessful) {
FirestoreClass().getUserDetails(this)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.


        // Redirect the user to Main Screen after log in.
        if (user.profilecompleted==0){
            val intent=Intent(this,UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)

        }
        else{
            startActivity(Intent(this,Dashboard::class.java))
        }
        finish()
    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


}