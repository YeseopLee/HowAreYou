package com.example.howareyou.views.writing

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.howareyou.R
import com.example.howareyou.App
import com.example.howareyou.databinding.ActivityWritingBinding
import com.example.howareyou.util.OnSingleClickListener
import com.example.howareyou.views.BaseActivity
import com.example.howareyou.views.detail.DetailCommentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_writing.*

@AndroidEntryPoint
class WritingActivity : BaseActivity<ActivityWritingBinding>(R.layout.activity_writing) {

    private val writingViewModel by viewModels<WritingViewModel>()
    private lateinit var board_id: String

    private lateinit var writingAdapter: WritingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.viewModel = writingViewModel

        initAdapter()
        initListener()
    }

    private fun initAdapter() {

        writingAdapter = WritingAdapter(this)
        binding.writingRecyclerviewImage.adapter = writingAdapter

    }

    private fun initListener(){

        // alert dialog value
        val builder = AlertDialog.Builder(this).create()

        writing_button_imageupload.setOnClickListener {
            writingViewModel.addImage()
        }

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

//    private fun attemptPost() {
//        writing_edittext_title.error = null
//        writing_edittext_content.error = null
//        val title: String = writing_edittext_title.text.toString()
//        val content: String = writing_edittext_content.text.toString()
//        var cancel = false
//        var focusView: View? = null
//        // 제목 유효성 검사
//        if (title.isEmpty()) {
//            writing_edittext_title.error = "제목을 입력하세요."
//            focusView = writing_edittext_title
//            cancel = true
//        }
//        // 본문 유효성 검사
//        if (content.isEmpty()) {
//            writing_edittext_content.error = "내용을 입력하세요."
//            focusView = writing_edittext_content
//            cancel = true
//        }
//        // 게시판 유효성 검사
//        if (App.prefs.myCode == ""){
//            Toast.makeText(this,"게시판을 선택하세요.",Toast.LENGTH_SHORT).show()
//            cancel = true
//        }
//        if (cancel) {
//            focusView?.requestFocus()
//        } else {
//            startPost(PostingDTO(
//                App.prefs.myEmail,
//                App.prefs.myId,
//                App.prefs.myName,title,content,"header",
//                App.prefs.myCode))
//        }
//    }

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
