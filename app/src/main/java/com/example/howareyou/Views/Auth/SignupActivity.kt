package com.example.howareyou.views.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.howareyou.R
import com.example.howareyou.databinding.ActivitySignupBinding
import com.example.howareyou.views.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_signup.*


@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(R.layout.activity_signup) {

    private val signInViewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.viewModel = signInViewModel

        movePage()

    }



    private fun movePage() {
        signInViewModel.moveSigninPage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        })
    }


}