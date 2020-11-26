package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.howareyou.Util.App
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.Model.SigninDTO
import com.example.howareyou.Model.SigninResponseDTO
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.Util.PreferenceUtil
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SigninActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    lateinit var prefs: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        // sharedpref 연결
        prefs = PreferenceUtil(applicationContext)

        // 자동로그인 시도
        moveMainpage()

        // button 관리
        signin_button_signin.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(view: View) {
                attemptLogin()
            }
        })

        signin_button_signup.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(view: View) {
                moveSignupPage()
            }
        })

    }

    private fun attemptLogin() {
        signin_edittext_id.setError(null)
        signin_edittext_password.setError(null)
        val email: String = signin_edittext_id.text.toString()
        val password: String = signin_edittext_password.text.toString()
        var cancel = false
        var focusView: View? = null
        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            signin_edittext_password.setError("비밀번호를 입력해주세요.")
            focusView = signin_edittext_password
            cancel = true
        } else if (!isPasswordValid(password)) {
            signin_edittext_password.setError("6자 이상의 비밀번호를 입력해주세요.")
            focusView = signin_edittext_password
            cancel = true
        }
        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            signin_edittext_id.setError("이메일을 입력해주세요.")
            focusView = signin_edittext_id
            cancel = true
        } else if (!isEmailValid(email)) {
            signin_edittext_id.setError("@를 포함한 유효한 이메일을 입력해주세요.")
            focusView = signin_edittext_id
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            startLogin(SigninDTO(email, password))
        }
    }

    private fun startLogin(data: SigninDTO) {
        service?.userLogin(data)?.enqueue(object : Callback<SigninResponseDTO?> {
            override fun onResponse(
                call: Call<SigninResponseDTO?>?,
                response: Response<SigninResponseDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: SigninResponseDTO = response.body()!!
                    //jwt 토큰 저장
                    App.prefs.myEmail = result.user.email
                    App.prefs.myJwt = result.jwt
                    App.prefs.myName = result.user.username
                    App.prefs.myId = result.user._id
                    Log.e("로그인 토큰 확인",App.prefs.myEmail+","+App.prefs.myJwt)
                    Log.e("유저 정보 확인", result.user.username+","+result.user._id)
                    moveMainpage()
                }else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<SigninResponseDTO> = gson.getAdapter<SigninResponseDTO>(
                        SigninResponseDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            showProgress(false)
                            val result : SigninResponseDTO = adapter.fromJson(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<SigninResponseDTO?>?, t: Throwable) {
                Log.e("로그인 에러 발생", t.message!!)
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun showProgress(show: Boolean) {
        signin_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
    }

    private fun moveMainpage() {
        System.out.println(App.prefs.myJwt+"jwt")
        if(App.prefs.myJwt != ""){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun moveSignupPage() {
        startActivity(Intent(this,SignupActivity::class.java))
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus

        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}
