package com.example.howareyou.views.Home

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.howareyou.App

class HomeFreeFragment : HomeBaseFragment() {

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress(true)
        App.prefs.myCode = App.prefs.codeFree
        postingDTOlist.clear()
        initAdapter()
        loadSelectedPosting()
    }

    override fun onResume() {
        super.onResume()
        Log.e("Free","OnResume")
        App.prefs.myCode = App.prefs.codeFree
        forceTouch()
    }

}

