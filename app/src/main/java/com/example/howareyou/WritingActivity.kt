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
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.howareyou.Model.*
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_writing.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WritingActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        // alert dialog value
        val builder = AlertDialog.Builder(this).create()

        // button 관리
        writing_button_check.setOnClickListener (object : OnSingleClickListener(){
            override fun onSingleClick(view: View) {
                attemptPost()
            }
        })

        writing_button_menu.setOnClickListener (object : OnSingleClickListener(){
            override fun onSingleClick(view: View) {

                val dialogView = layoutInflater.inflate(R.layout.activity_board_menu, null)
                val Btnfree = dialogView.findViewById<Button>(R.id.boardmenu_button_free)
                val Btnqa = dialogView.findViewById<Button>(R.id.boardmenu_button_qa)
                val Btntips = dialogView.findViewById<Button>(R.id.boardmenu_button_tips)
                val Btnstudy = dialogView.findViewById<Button>(R.id.boardmenu_button_study)

                builder.setView(dialogView)
                builder.show()

                Btnfree.setOnClickListener {
                    App.prefs.myCode = App.prefs.codeFree
                    writing_textview_writing.text = "자유게시판"
                    builder.dismiss()
                }
                Btnqa.setOnClickListener {
                    App.prefs.myCode = App.prefs.codeQA
                    writing_textview_writing.text = "Q&A"
                    builder.dismiss()
                }
                Btntips.setOnClickListener {
                    App.prefs.myCode = App.prefs.codeTips
                    writing_textview_writing.text = "Tips"
                    builder.dismiss()
                }
                Btnstudy.setOnClickListener {
                    App.prefs.myCode = App.prefs.codeStudy
                    writing_textview_writing.text = "스터디/모임"
                    builder.dismiss()
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()

        when(App.prefs.myCode){
            App.prefs.codeFree -> writing_textview_writing.text = "자유게시판"
            App.prefs.codeQA -> writing_textview_writing.text = "Q&A"
            App.prefs.codeTips -> writing_textview_writing.text = "Tips"
            App.prefs.codeStudy -> writing_textview_writing.text = "스터디/모임"
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
            startPost(PostingDTO(App.prefs.myEmail,App.prefs.myId,App.prefs.myName,title,content,"header",App.prefs.myCode))
        }
    }

    private fun startPost(data: PostingDTO) {
        service?.userPost("Bearer "+App.prefs.myJwt,data)?.enqueue(object : Callback<PostingResponseDTO?> {
            override fun onResponse(
                call: Call<PostingResponseDTO?>?,
                response: Response<PostingResponseDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: PostingResponseDTO = response.body()!!
//                    movePostingPage()
                    finish()

                }else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<PostingResponseDTO> = gson.getAdapter<PostingResponseDTO>(
                        PostingResponseDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            showProgress(false)
                            val result : PostingResponseDTO = adapter.fromJson(response.errorBody()!!.string())
                            System.out.println(result.message)

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
                Log.e("error", t.message!!)
            }
        })
    }

    private fun showDiaglog(){
        val builder = AlertDialog.Builder(this)

        val dialogView = layoutInflater.inflate(R.layout.activity_board_menu, null)

        val Btnfree = dialogView.findViewById<Button>(R.id.boardmenu_button_free)
        val Btnqa = dialogView.findViewById<Button>(R.id.boardmenu_button_qa)
        val Btntips = dialogView.findViewById<Button>(R.id.boardmenu_button_tips)
        val Btnstudy = dialogView.findViewById<Button>(R.id.boardmenu_button_study)
    }

    private fun showProgress(show: Boolean) {
        writing_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
    }

    private fun movePostingPage() {

        var Fragment = PostingFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_framelayout, Fragment).commit()

//        var It = Intent(applicationContext,PostingActivity::class.java)
//        startActivity(It)
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
