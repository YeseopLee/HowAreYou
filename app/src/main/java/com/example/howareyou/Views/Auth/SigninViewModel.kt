package com.example.howareyou.views.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.*
import com.example.howareyou.util.Event
import com.example.howareyou.repository.AuthRepository
import com.example.howareyou.util.CoroutineHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SigninViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _moveMainPage = MutableLiveData<Event<Boolean>>()
    val  moveMainPage: LiveData<Event<Boolean>> = _moveMainPage

    private val _moveSignupPage = MutableLiveData<Event<Boolean>>()
    val  moveSignupPage: LiveData<Event<Boolean>> = _moveSignupPage

    private val _moveFindPage = MutableLiveData<Event<Boolean>>()
    val  moveFindPage: LiveData<Event<Boolean>> = _moveFindPage

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(context.applicationContext, "로그인 정보를 확인해주세요.", Toast.LENGTH_SHORT).show()
    }

    init {
        email.value = ""
        password.value = ""
    }

    fun attemptLogin() {

        startLogin(SigninDTO(email.value!!, password.value!!))

    }


    fun startLogin(data: SigninDTO) {

        viewModelScope.launch(exceptionHandler) {
            val loginInfo = authRepository.login(data)

            // 유저 정보 sharedpref 저장
            App.prefs.myEmail = loginInfo.user.email
            App.prefs.myJwt = loginInfo.jwt
            App.prefs.myName = loginInfo.user.username
            App.prefs.myId = loginInfo.user._id

            moveMainPage()
        }

    }

    //기존 회원이 기기변경 했을때
//    fun postDeviceToken(data: PostdeviceTokenDTO) {
//        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
//            authRepository.postDeviceToken(data)
//        }
//    }

    fun moveMainPage() {
        _moveMainPage.value = Event(true)
    }

    fun moveSignupPage() {
        _moveSignupPage.value = Event(true)
    }

    fun moveFindPage() {
        _moveFindPage.value = Event(true)
    }

}




//
//private fun startLogin(data: SigninDTO) {
//    service?.userLogin(data)?.enqueue(object : Callback<SigninResponseDTO?> {
//        override fun onResponse(
//            call: Call<SigninResponseDTO?>?,
//            response: Response<SigninResponseDTO?>
//
//        ) {
//            if(response.isSuccessful)
//            {
//                val result: SigninResponseDTO = response.body()!!
//                //jwt 토큰 저장
//                App.prefs.myEmail = result.user.email
//                App.prefs.myJwt = result.jwt
//                App.prefs.myName = result.user.username
//                App.prefs.myId = result.user._id
//                Log.e("로그인 토큰 확인", App.prefs.myEmail+","+ App.prefs.myJwt)
//                Log.e("유저 정보 확인", result.user.username+","+result.user._id)
//
//                moveMainpage()
//            }else {
//                // 실패시 resopnse.errorbody를 객체화
//                val gson = Gson()
//                val adapter: TypeAdapter<SigninResponseDTO> = gson.getAdapter<SigninResponseDTO>(
//                    SigninResponseDTO::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        showProgress(false)
//                        val result : SigninResponseDTO = adapter.fromJson(response.errorBody()!!.string())
//
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//        }
//
//        override fun onFailure(call: Call<SigninResponseDTO?>?, t: Throwable) {
//            Log.e("로그인 에러 발생", t.message!!)
//        }
//    })
//}

//
//
//private fun isEmailValid(email: String): Boolean {
//    return email.contains("@")
//}
//
//private fun isPasswordValid(password: String): Boolean {
//    return password.length >= 6
//}
//
//private fun showProgress(show: Boolean) {
//    signin_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
//}
//

//
//private fun moveSignupPage() {
//    startActivity(Intent(this, SignupActivity::class.java))
//}
