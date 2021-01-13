package com.example.howareyou.views.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentHomeBinding
import com.example.howareyou.databinding.FragmentHomeViewpagerBinding
import com.example.howareyou.views.auth.AccountActivity
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.views.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){

    private val homeViewModel by viewModels<HomeViewModel>()

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = homeViewModel

        val pagerAdapter = HomeViewAdapter(requireActivity())
        home_viewpager.adapter = pagerAdapter

        //showProgress(false)
        initListener(view)
        initTab()

    }

    private inner class HomeViewAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 6

        override fun createFragment(position: Int): Fragment {

            return if (position == 0) {
                HomeAllFragment()
            } else if (position == 1) {
                HomeFreeFragment()
            } else if (position == 2) {
                HomeQAFragment()
            } else if (position == 3) {
                HomeTipsFragment()
            } else if (position == 4) {
                HomeStudyFragment()
            } else {
                HomeBestFragment()
            }
        }
    }

    private fun initListener(view: View){
        view.home_button_myaccount.setOnClickListener {
            startActivity(Intent(activity, AccountActivity::class.java))
        }
    }

    private fun initTab(){

        TabLayoutMediator(home_tablayout, home_viewpager) {
                tab, position ->
            when(position) {
                0 -> {tab.setText(getString(R.string.allboard)) }
                1 -> {tab.setText(getString(R.string.freeboard))}
                2 -> {tab.setText(getString(R.string.qna))}
                3 -> {tab.setText(getString(R.string.tipsboard))}
                4 -> {tab.setText(getString(R.string.studyboard)) }
                5 -> {tab.setText(getString(R.string.bestboard))}
            }
        }.attach()

    }

}

