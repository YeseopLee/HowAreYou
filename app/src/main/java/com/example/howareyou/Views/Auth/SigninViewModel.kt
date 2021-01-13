package com.example.howareyou.views.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.*
import com.example.howareyou.util.Event
import com.example.howareyou.repository.AuthRepository
import kotlinx.coroutines.launch

class SigninViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _moveMainPage = MutableLiveData<Event<Boolean>>()
    val  moveMainPage: LiveData<Event<Boolean>> = _moveMainPage

    private val _moveSignupPage = MutableLiveData<Event<Boolean>>()
    val  moveSignupPage: LiveData<Event<Boolean>> = _moveSignupPage

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    init {
        email.value = ""
        password.value = ""
    }

    fun attemptLogin() {

        startLogin(SigninDTO(email.value!!, password.value!!))

    }

    fun startLogin(data: SigninDTO) {

        viewModelScope.launch {
            val loginInfo = authRepository.login(data)

            // 유저 정보 sharedpref 저장
            App.prefs.myEmail = loginInfo.user.email
            App.prefs.myJwt = loginInfo.jwt
            App.prefs.myName = loginInfo.user.username
            App.prefs.myId = loginInfo.user._id

            moveMainPage()
        }

    }


    fun postDeviceToken(data: PostdeviceTokenDTO) {
        viewModelScope.launch {
            authRepository.postDeviceToken(data)
        }
    }

    fun moveMainPage() {
        _moveMainPage.value = Event(true)
    }

    fun moveSignupPage() {
        _moveSignupPage.value = Event(true)
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
