package com.example.howareyou

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gagaotalk.Model.SignupDTO
import com.example.gagaotalk.Model.SignupResponseDTO
import com.example.gagaotalk.network.RetrofitClient
import com.example.gagaotalk.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.StringReader


class SignupActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        signup_button_signup.setOnClickListener {
            attemptJoin()
        }
    }

    private fun attemptJoin() {
        //inputlayout.error = 인풋 오류시 에러메시지 출력
        signup_textinputlayout_email.error = null
        signup_textinputlayout_password.error = null
        val email: String = signup_edittext_email.text.toString()
        val username: String = signup_edittext_username.text.toString()
        val password: String = signup_edittext_password.text.toString()
        var cancel = false
        var focusView: View? = null
        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            signup_edittext_password.error = "비밀번호를 입력해주세요."
            focusView = signup_edittext_password
            cancel = true
        } else if (!isPasswordValid(password)) {
            signup_edittext_password.error = "6자 이상의 비밀번호를 입력해주세요."
            focusView = signup_edittext_password
            cancel = true
        }
        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            signup_edittext_email.error = "아이디를 입력해주세요."
            focusView = signup_edittext_email
            cancel = true
        } else if (!isEmailValid(email)) {
            signup_edittext_email.setError("@를 포함한 유효한 이메일을 입력해주세요.")
            focusView = signup_edittext_email
            cancel = true
        }

        // 닉네임의 유효성 검사
        if (username.isEmpty()){
            signup_edittext_username.error = "닉네임을 입력해주세요."
            focusView = signup_edittext_username
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            startJoin(SignupDTO(email, username, password))
            showProgress(true)
        }
    }

    private fun startJoin(data: SignupDTO) {
        service?.userJoin(data)?.enqueue(object : Callback<SignupResponseDTO?> {
            override fun onResponse(
                call: Call<SignupResponseDTO?>?,
                response: Response<SignupResponseDTO?>
            ) {
                Log.e("???","?????")
                if(response.isSuccessful) {
                    val result: SignupResponseDTO = response.body()!!
                    showProgress(false)
                    finish()
                }
                else if (response.code() == 400) {
                    val gson = Gson()
                    val adapter: TypeAdapter<SignupResponseDTO> = gson.getAdapter<SignupResponseDTO>(
                        SignupResponseDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            showProgress(false)
                            val result :SignupResponseDTO = adapter.fromJson(response.errorBody()!!.string())
                            System.out.println("hihihihi"+result)
                            val errMessage : String = result.message[0].messages[0].message

                            Toast.makeText(applicationContext, errMessage, Toast.LENGTH_SHORT).show()

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponseDTO?>, t: Throwable) {
                Log.e("회원가입 에러 발생", t.message);
                showProgress(false);
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
        signup_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
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
