package com.example.howareyou

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.howareyou.Model.PostingDTO
import com.example.howareyou.Model.PostingResponseDTO
import com.example.howareyou.Model.SigninDTO
import com.example.howareyou.Model.SigninResponseDTO
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_writing.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WritingActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        writing_button_check.setOnClickListener {
            attemptPost()
        }
    }

    private fun attemptPost() {
        writing_edittext_title.error = null
        writing_edittext_content.error = null
        val title: String = writing_edittext_title.text.toString()
        val content: String = writing_edittext_content.text.toString()
        var cancel = false
        var focusView: View? = null
        // 제목 유효성 검사
        if (title.isEmpty()) {
            writing_edittext_title.error = "제목을 입력하세요."
            focusView = writing_edittext_title
            cancel = true
        }
        // 본문 유효성 검사
        if (content.isEmpty()) {
            writing_edittext_content.error = "내용을 입력하세요."
            focusView = writing_edittext_content
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            startPost(PostingDTO(
                "email","category","header",title,"author",content,0,0,0,false,0,""))
        }
    }

    private fun startPost(data: PostingDTO) {
        service?.userPost(data)?.enqueue(object : Callback<PostingResponseDTO?> {
            override fun onResponse(
                call: Call<PostingResponseDTO?>?,
                response: Response<PostingResponseDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: PostingResponseDTO = response.body()!!
                    System.out.println("????11?")
                    System.out.println(result.toString())

                }else {
                    System.out.println(response.code().toString())
                    System.out.println(response.message().toString())
                    System.out.println(response.body().toString())
                    System.out.println(response.errorBody().toString())
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<PostingResponseDTO> = gson.getAdapter<PostingResponseDTO>(
                        PostingResponseDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            showProgress(false)
                            val result : PostingResponseDTO = adapter.fromJson(response.errorBody()!!.string())
                            System.out.println(result.toString())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
                System.out.println("????sdfsdf?")
                Log.e("로그인 에러 발생", t.message!!)
            }
        })
    }

    private fun showProgress(show: Boolean) {
        writing_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
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
