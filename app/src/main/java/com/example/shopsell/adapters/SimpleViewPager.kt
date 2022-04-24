package com.example.shopsell.adapters

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.shopsell.activity.ui.dashboard.DashboardFragment
import com.example.shopsell.activity.ui.home.MyProductsFragment
import com.example.shopsell.activity.ui.notifications.OrdersFragment


class SimpleFragmentPagerAdapter(
    fm: FragmentManager?
) : FragmentPagerAdapter(fm!!){

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DashboardFragment()
        } else if (position == 1) {
            MyProductsFragment()
        } else {
            OrdersFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }




}