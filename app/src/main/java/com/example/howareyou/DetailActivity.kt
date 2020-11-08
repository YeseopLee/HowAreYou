package com.example.howareyou

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.CommentDTO
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*


class DetailActivity : AppCompatActivity() {

    lateinit var detailAdapter: DetailAdapter

    var commentDTOlist : ArrayList<CommentDTO> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        detail_textview_content.text = intent.getStringExtra("content")

        commentDTOlist.add(CommentDTO(0,0,0,"hi"))
        commentDTOlist.add(CommentDTO(0,0,0,"hi2"))
        commentDTOlist.add(CommentDTO(0,0,0,"hi3"))

        detailAdapter = DetailAdapter(this,commentDTOlist)
        detail_recyclerview.adapter = detailAdapter

        val lm = LinearLayoutManager(this)
        detail_recyclerview.layoutManager = lm
        detail_recyclerview.setHasFixedSize(true)

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
