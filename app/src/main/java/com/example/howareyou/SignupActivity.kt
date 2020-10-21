package com.example.howareyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.gagaotalk.Model.SignupDTO
import com.example.gagaotalk.Model.SignupResponseDTO
import com.example.gagaotalk.network.RetrofitClient
import com.example.gagaotalk.network.ServiceApi
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        Log.e("service???",service.toString())

        signup_button_signup.setOnClickListener {
            attemptJoin()
        }
    }

    private fun attemptJoin() {
        //inputlayout.error = 인풋 오류시 에러메시지 출력
        signup_textinputlayout_email.error = null
        signup_textinputlayout_password.error = null
        val email: String = signup_edittext_email.text.toString()
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
        if (cancel) {
            focusView?.requestFocus()
        } else {
            startJoin(SignupDTO(email, password))
            showProgress(true)
        }
    }

    private fun startJoin(data: SignupDTO) {
        service?.userJoin(data)?.enqueue(object : Callback<SignupResponseDTO?> {
            override fun onResponse(
                call: Call<SignupResponseDTO?>?,
                response: Response<SignupResponseDTO?>
            ) {
                val result: SignupResponseDTO = response.body()!!
                val t1 = Toast.makeText(applicationContext,result.message, Toast.LENGTH_SHORT)
                t1.show()
                showProgress(false)
                if (result.code === 200) {
                    finish()
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
}
