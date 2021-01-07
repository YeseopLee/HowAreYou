package com.example.howareyou.views.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {

        if (position == 0) {
            return HomeAllFragment()
        }
//            else if (position == 1) {
//                return com.example.howareyou.HomeFreeFragment()
//            } else if (position == 2) {
//                return com.example.howareyou.HomeQAFragment()
//            } else if (position == 3) {
//                return com.example.howareyou.HomeTipsFragment()
//            } else if (position == 4) {
//                return com.example.howareyou.HomeStudyFragment()
//            } else {
//                return com.example.howareyou.HomeBestFragment()
//            }
        else return HomeAllFragment()
    }
}