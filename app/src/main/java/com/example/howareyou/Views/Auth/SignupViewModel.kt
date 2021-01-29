package com.example.howareyou.views.auth

import android.content.Context
import android.view.View
import android.widget.Toast
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
import com.example.howareyou.util.CoroutineHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


class SignupViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _moveSigninPage = MutableLiveData<Event<Boolean>>()
    val  moveSigninPage: LiveData<Event<Boolean>> = _moveSigninPage

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var username = MutableLiveData<String>()

    lateinit var _email : String
    lateinit var _password : String
    lateinit var _username : String

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(context.applicationContext, "이미 가입된 정보입니다.", Toast.LENGTH_SHORT).show()
    }

    init {
        email.value = ""
        password.value = ""
        username.value = ""
    }

//    fun attemptJoin(){
//        startJoin(SignupDTO(email.value!!, username.value!!, password.value!!))
//    }


    fun attemptJoin() {
        //inputlayout.error = 인풋 오류시 에러메시지 출력

        var cancel = false
        var focusView: View? = null
        // 패스워드의 유효성 검사
        if (password.value?.isEmpty()!!) {
            Toast.makeText(context.applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            cancel = true
        }
        else if (!isPasswordValid(password.value!!)) {
            Toast.makeText(context.applicationContext, "6자 이상의 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            cancel = true
        }
        // 이메일의 유효성 검사
        if (email.value?.isEmpty()!!) {
            Toast.makeText(context.applicationContext, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
            cancel = true
        } else if (!isEmailValid(email.value!!)) {
            Toast.makeText(context.applicationContext, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            cancel = true
        }

        // 닉네임의 유효성 검사
        if (username.value?.isEmpty()!!){
            Toast.makeText(context.applicationContext, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            Toast.makeText(context.applicationContext, "해당 이메일로 인증을 완료하세요.", Toast.LENGTH_SHORT).show()
            startJoin(SignupDTO(email.value!!, username.value!!, password.value!!))
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }


    fun startJoin(data: SignupDTO) {

        viewModelScope.launch(exceptionHandler) {
            val signupInfo = authRepository.signup(data)
            postDeviceToken(PostdeviceTokenDTO(signupInfo.user._id, App.prefs.myDevice, true))
        }
    }

    fun postDeviceToken(data: PostdeviceTokenDTO) {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            authRepository.postDeviceToken(data)

            moveSignInPage()
        }
    }


    fun moveSignInPage() {
        _moveSigninPage.value = Event(true)
    }

}
