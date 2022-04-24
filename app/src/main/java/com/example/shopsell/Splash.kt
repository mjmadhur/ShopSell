package com.example.shopsell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.shopsell.activity.Dashboard
import com.example.shopsell.activity.LogIn
import com.example.shopsell.firestore.FirestoreClass
import com.example.shopsell.model.User

class Splash : AppCompatActivity() {
    private var muser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            val Currentuserid= FirestoreClass().getcurrentuserid()
           /* if (Currentuserid.isNotEmpty()) {
                // Start the Main Activity
                startActivity(Intent(this, Dashboard::class.java))
            } else {
                // Start the Intro Activity
                startActivity(Intent(this, LogIn::class.java))
            }*/
            muser=User()
if (muser!!.profilecompleted==0){
    startActivity(Intent(this,LogIn::class.java))
}


else{
    startActivity(Intent(this,Dashboard::class.java))
}


            finish()
        },1500)
    }

}