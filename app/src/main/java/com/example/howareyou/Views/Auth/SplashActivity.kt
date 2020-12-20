package com.example.howareyou.Views.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.howareyou.R
import com.example.howareyou.Util.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // token 처리
        // jwt 토큰 확인하고,

        getFCMToken()

        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)

    }

    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("get Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("get token", token)

            App.prefs.myDevice = token!!
            System.out.println("mytoken"+App.prefs.myDevice)
        })
    }

}