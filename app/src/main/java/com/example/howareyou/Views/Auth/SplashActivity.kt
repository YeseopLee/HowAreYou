package com.example.howareyou.views.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.howareyou.R
import com.example.howareyou.App
import com.example.howareyou.databinding.ActivitySplashBinding
import com.example.howareyou.views.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){

    val SPLASH_VIEW_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)

    }


}