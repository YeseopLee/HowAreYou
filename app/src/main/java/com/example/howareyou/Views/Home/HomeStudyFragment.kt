package com.example.howareyou.Views.Home

import android.os.Bundle
import android.view.View
import com.example.howareyou.Util.App


class HomeStudyFragment : HomeBaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.prefs.myCode = App.prefs.codeStudy
        initAdapter()
        loadSelectedPosting()
    }

    override fun onResume() {
        super.onResume()
        App.prefs.myCode = App.prefs.codeStudy

        forceTouch()
    }


}
