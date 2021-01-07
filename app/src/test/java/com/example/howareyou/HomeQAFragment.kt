package com.example.howareyou

import android.os.Bundle
import android.view.View

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

