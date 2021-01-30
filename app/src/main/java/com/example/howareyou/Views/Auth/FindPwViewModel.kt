package com.example.howareyou.views.auth

import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.model.FindPasswordDTO
import com.example.howareyou.repository.AuthRepository
import com.example.howareyou.util.Event
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


class FindPwViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _moveSigninPage = MutableLiveData<Event<Boolean>>()
    val  moveSigninPage: LiveData<Event<Boolean>> = _moveSigninPage

    val email = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(context.applicationContext, "회원정보를 확인해주세요.", Toast.LENGTH_SHORT).show()
    }

    fun moveSigninPage() {
        _moveSigninPage.value = Event(true)
    }

    fun findPassword() {
        viewModelScope.launch(exceptionHandler) {
            val findInfo = authRepository.findPw(FindPasswordDTO(email.value!!))
            Toast.makeText(context.applicationContext, "이메일로 임시 비밀번호를 발급하였습니다.", Toast.LENGTH_SHORT).show()
            moveSigninPage()
        }
    }

}