package com.example.howareyou.Views.Home

import android.os.Bundle
import android.view.View
import com.example.howareyou.Util.App

class HomeTipsFragment : HomeBaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.prefs.myCode = App.prefs.codeTips
        initAdapter()
        loadSelectedPosting()
    }

    override fun onResume() {
        super.onResume()
        App.prefs.myCode = App.prefs.codeTips
        forceTouch()
    }


}