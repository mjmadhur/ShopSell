package com.example.shopsell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            @Suppress("Deprecation")
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            @Suppress("Deprecation")
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }
}