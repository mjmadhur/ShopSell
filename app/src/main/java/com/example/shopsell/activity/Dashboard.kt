package com.example.shopsell.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.example.shopsell.databinding.ActivityDashboardBinding
import com.myshoppal.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard.*



import androidx.viewpager.widget.ViewPager as ViewPager


import android.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout

import com.example.shopsell.adapters.SimpleFragmentPagerAdapter


import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.R.layout
import com.example.shopsell.R
import com.example.shopsell.adapters.ScrollHandler


class Dashboard : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        setContentView(R.layout.activity_dashboard)




        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val navView: BottomNavigationView = findViewById(com.example.shopsell.R.id.nav_view)






        val navController = findNavController(com.example.shopsell.R.id.nav_host_fragment)
        //findViewById<BottomNavigationView>(R.id.nav_view)
           //.setupWithNavController(navController)
        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                com.example.shopsell.R.id.navigation_dashboard -> showBottomNav()
                com.example.shopsell.R.id.navigation_Orders -> showBottomNav()
                R.id.navigation_sold_products->showBottomNav()
                else -> hideBottomNav()
            }
        }*/



        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard
                , R.id.navigation_Products, R.id.navigation_Orders,R.id.navigation_sold_products
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onBackPressed() {

        doubleBackToExit()
    }
    private fun showBottomNav() {
        nav_view.visibility = View.VISIBLE

    }
    private fun hideBottomNav() {
        nav_view.visibility = View.GONE

    }

}