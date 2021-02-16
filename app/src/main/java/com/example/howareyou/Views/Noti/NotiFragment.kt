package com.example.howareyou.views.noti

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.views.auth.AccountActivity
import com.example.howareyou.model.NotiItem
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentNotificationBinding
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.view.*

@AndroidEntryPoint
class NotiFragment : BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification){

    private val notiViewModel by viewModels<NotiViewModel>()

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = notiViewModel
        initAdapter()

    }



//    override fun onRefresh() {
//        // 데이터 list 초기화
//        notiDTOList.clear()
//        loadNotification()
//        notification_swipelayout.isRefreshing = false
//    }

    override fun onResume() {
        super.onResume()

        notiViewModel.initData()

    }

    private fun initAdapter(){

        //어댑터 연결
        val notiAdapter = NotiAdapter(requireActivity(),notiViewModel)
        notification_recyclerview.adapter = notiAdapter
        val lm = LinearLayoutManager(activity)
        notification_recyclerview.layoutManager = lm
        notification_recyclerview.setHasFixedSize(true)

        // 역순출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

    }



}

