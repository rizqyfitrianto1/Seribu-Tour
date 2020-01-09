package com.example.belajarmaps.Admin.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.belajarmaps.Admin.Fragment.OrderActiveFragment
import com.example.belajarmaps.Admin.Fragment.OrderUsedFragment

class MyPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    private val pages = listOf(
        OrderActiveFragment(),
        OrderUsedFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Active"
            else -> "Used"
        }
    }
}