package com.example.howareyou.views.home

import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentHomeViewpagerBinding
import com.example.howareyou.views.BaseFragment

abstract class BaseHomeFragment(code: String) : BaseFragment<FragmentHomeViewpagerBinding>(R.layout.fragment_home_viewpager) {

    private val homePagerViewModel by viewModels<HomePagerViewModel>()
    lateinit var homeAdapter: HomeAdapter
    val code = code

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.prefs.myCode = code
        binding.viewModel = homePagerViewModel
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        App.prefs.myCode = code
        forceTouch()
    }

    private fun initAdapter() {

        homeAdapter = HomeAdapter(requireActivity())
        binding.viewpagerRecyclerview.adapter = homeAdapter
    }

    fun forceTouch(){

        val downTime: Long = SystemClock.uptimeMillis()
        val eventTime: Long = SystemClock.uptimeMillis()
        val down_event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
        val up_event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 0f, 0f, 0)

        binding.viewpagerSwipelayout.dispatchTouchEvent(down_event)
        binding.viewpagerSwipelayout.dispatchTouchEvent(up_event)
    }

}