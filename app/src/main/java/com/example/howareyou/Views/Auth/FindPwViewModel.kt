package com.example.howareyou.views.auth

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.howareyou.repository.AuthRepository
import com.example.howareyou.util.Event
import dagger.hilt.android.qualifiers.ApplicationContext


class FindPwViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _moveSigninPage = MutableLiveData<Event<Boolean>>()
    val  moveSigninPage: LiveData<Event<Boolean>> = _moveSigninPage

    val email = MutableLiveData<String>()

    fun moveMainPage() {
        _moveSigninPage.value = Event(true)
    }

    fun findPassword() {

    }

}