package com.example.howareyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Util.App
import com.example.howareyou.Util.EndlessRecyclerViewScrollListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.android.material.tabs.TabLayout
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

        val pagerAdapter = HomeViewAdapter(activity!!,6)
        home_viewpager.adapter = pagerAdapter

        showProgress(false)
        initListener(view)
        initTab()
    }

    private inner class HomeViewAdapter(fa: FragmentActivity, pagenum: Int) : FragmentStateAdapter(
        fa
    ) {

        var mCount = pagenum

        override fun getItemCount(): Int = 6

        override fun createFragment(position: Int): Fragment {
            val index = getRealPosition(position)

            if (index == 0) {
                Log.e("position", position.toString())
                return HomeAllFragment()
            } else if (index == 1) {
                Log.e("position", position.toString())
                return HomeFreeFragment()
            } else if (index == 2) {
                Log.e("position", position.toString())
                return HomeQAFragment()
            } else if (index == 3) {
                Log.e("position", position.toString())
                return HomeTipsFragment()
            } else if (index == 4) {
                Log.e("position", position.toString())
                return HomeStudyFragment()
            } else {
                Log.e("position", position.toString())
                return HomeBestFragment()
            }

        }

        fun getRealPosition(position: Int): Int {
            return position % mCount
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
                0 -> {tab.setText(getString(R.string.allboard))}
                1 -> {tab.setText(getString(R.string.freeboard))}
                2 -> {tab.setText(getString(R.string.qna))}
                3 -> {tab.setText(getString(R.string.tipsboard))}
                4 -> {tab.setText(getString(R.string.studyboard))}
                5 -> {tab.setText(getString(R.string.bestboard))}
            }
        }.attach()

    }

    private fun showProgress(show: Boolean){
        home_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
    }


}

