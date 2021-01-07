package com.example.howareyou.views.Home

import android.os.Bundle
import android.view.View
import com.example.howareyou.App

class HomeBestFragment : HomeBaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.prefs.myCode = App.prefs.codeBest
        initAdapter()
        loadSelectedPosting()
    }

    override fun onResume() {
        super.onResume()
        App.prefs.myCode = App.prefs.codeBest
        forceTouch()
    }


}