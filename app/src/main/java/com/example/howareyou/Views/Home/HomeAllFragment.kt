package com.example.howareyou.views.home

import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentHomeViewpagerBinding
import com.example.howareyou.views.BaseFragment
import com.example.howareyou.views.BaseHomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_viewpager.*

@AndroidEntryPoint
class HomeAllFragment : BaseHomeFragment(App.prefs.key_all) {


    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.prefs.myCode = App.prefs.key_all
    }

}

