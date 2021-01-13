package com.example.howareyou.views.home

import android.os.Bundle
import android.view.View
import com.example.howareyou.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBestFragment : BaseHomeFragment(App.prefs.codeBest) {


    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}