package com.example.howareyou.views.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.howareyou.R
import com.example.howareyou.databinding.ActivityFindpwBinding
import com.example.howareyou.databinding.ActivitySigninBinding
import com.example.howareyou.views.BaseActivity
import com.example.howareyou.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FindPwActivity : BaseActivity<ActivityFindpwBinding>(R.layout.activity_findpw) {

    private val findPwViewModel by viewModels<FindPwViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpw)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.viewModel = findPwViewModel

        moveSigninPage()
    }

    private fun moveSigninPage() {
        findPwViewModel.moveSigninPage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        })
    }


}
