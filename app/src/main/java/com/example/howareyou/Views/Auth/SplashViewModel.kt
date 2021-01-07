package com.example.howareyou.views.auth

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.howareyou.App
import com.example.howareyou.repository.AuthRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class SplashViewModel @ViewModelInject constructor(
) : ViewModel() {

    init {
        getFCMToken()
    }

    fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("get Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("get token", token)

            App.prefs.myDevice = token!!
            System.out.println("mytoken"+ App.prefs.myDevice)
        })
    }
}