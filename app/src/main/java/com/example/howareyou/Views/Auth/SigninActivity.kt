package com.example.howareyou.views.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.howareyou.views.MainActivity
import com.example.howareyou.R
import com.example.howareyou.views.BaseActivity
import com.example.howareyou.databinding.ActivitySigninBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigninActivity : BaseActivity<ActivitySigninBinding>(R.layout.activity_signin) {

    private val signInViewModel by viewModels<SigninViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.viewModel = signInViewModel

        moveMainPage()
        moveSignupPage()
        moveFindPage()
    }

    private fun moveMainPage() {
        signInViewModel.moveMainPage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                startActivity(Intent(this, MainActivity::class.java))
            }
        })
    }

    private fun moveSignupPage() {
        signInViewModel.moveSignupPage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                startActivity(Intent(this, SignupActivity::class.java))
            }
        })
    }

    private fun moveFindPage() {
        signInViewModel.moveFindPage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                startActivity(Intent(this, FindPwActivity::class.java))
            }
        })
    }


}
