package com.example.howareyou.views.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.PostdeviceTokenDTO
import com.example.howareyou.model.SignupDTO
import com.example.howareyou.util.Event
import com.example.howareyou.repository.AuthRepository
import kotlinx.coroutines.launch


class SignupViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _moveSigninPage = MutableLiveData<Event<Boolean>>()
    val  moveSigninPage: LiveData<Event<Boolean>> = _moveSigninPage

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var username = MutableLiveData<String>()

    init {
        email.value = ""
        password.value = ""
        username.value = ""
    }

    fun attemptJoin(){
        startJoin(SignupDTO(email.value!!, username.value!!, password.value!!))
    }


    fun startJoin(data: SignupDTO) {

        viewModelScope.launch {
            val signupInfo = authRepository.signup(data)
            postDeviceToken(PostdeviceTokenDTO(signupInfo.user._id, App.prefs.myDevice, true))
        }
    }

    fun postDeviceToken(data: PostdeviceTokenDTO) {
        viewModelScope.launch {
            authRepository.postDeviceToken(data)

            moveSignInPage()
        }
    }


    fun moveSignInPage() {
        _moveSigninPage.value = Event(true)
    }

}
