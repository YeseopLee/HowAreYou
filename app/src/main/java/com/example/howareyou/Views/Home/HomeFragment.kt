package com.example.howareyou.Views.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.howareyou.Views.Auth.AccountActivity
import com.example.howareyou.R
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private var service: ServiceApi? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)
        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)


        return view
    }

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = HomeViewAdapter(activity!!)
        home_viewpager.adapter = pagerAdapter

        //showProgress(false)
        initListener(view)
        initTab()
    }

    private inner class HomeViewAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 6

        override fun createFragment(position: Int): Fragment {

            if (position == 0) {
                return HomeAllFragment()
            } else if (position == 1) {
                return HomeFreeFragment()
            } else if (position == 2) {
                return HomeQAFragment()
            } else if (position == 3) {
                return HomeTipsFragment()
            } else if (position == 4) {
                return HomeStudyFragment()
            } else {
                return HomeBestFragment()
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

//    private fun showProgress(show: Boolean){
//        home_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
//    }


}

