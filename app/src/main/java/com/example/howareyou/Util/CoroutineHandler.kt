package com.example.howareyou.util

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler

class CoroutineHandler {

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("Exception Error",exception.message)
        Log.e("Exception Error",exception.localizedMessage)
        Log.e("Exception Error",exception.cause.toString())
        Log.e("Exception Error",exception.stackTrace.toString())
        exception.printStackTrace()
    }
}