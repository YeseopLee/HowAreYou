package com.example.howareyou.views.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentHomeViewpagerBinding
import com.example.howareyou.views.BaseFragment
import com.example.howareyou.views.BaseHomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeTipsFragment : BaseHomeFragment(App.prefs.codeTips) {


    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}