package com.example.howareyou.views.Home

import android.os.Bundle
import android.view.View
import com.example.howareyou.App

class HomeQAFragment : HomeBaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.prefs.myCode = App.prefs.codeQA
        initAdapter()
        loadSelectedPosting()
    }

    override fun onResume() {
        super.onResume()
        App.prefs.myCode = App.prefs.codeQA
        forceTouch()
    }


}

