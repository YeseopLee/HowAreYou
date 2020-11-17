package com.example.howareyou

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.Comment
import com.example.howareyou.Model.CommentDTO
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Util.App
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class DetailActivity : AppCompatActivity() {

    private var service: ServiceApi? = null
    var commentDTOList : ArrayList<Comment> = arrayListOf()
    var mAdapter = DetailAdapter(this,commentDTOList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        // posting activity에서 클릭한 게시물의 id를 받아온다
        var board_id: String = intent.getStringExtra("board_id")
        loadPostingContent(board_id)

        detail_recyclerview_comment.adapter = mAdapter
        val lm = LinearLayoutManager(this)
        detail_recyclerview_comment.layoutManager = lm
        detail_recyclerview_comment.setHasFixedSize(true)

    }

    private fun loadPostingContent(board_id : String) {
        service?.getPostContent(board_id)?.enqueue(object : Callback<LoadPostItem?> {
            override fun onResponse(
                call: Call<LoadPostItem?>?,
                response: Response<LoadPostItem?>

            ) {

                if(response.isSuccessful)
                {
                    val result: LoadPostItem = response.body()!!

//                        showProgress(false)

                    detail_textview_title.text = result.title
                    detail_textview_content.text = result.content


                    //LoadPostItem의 comments를 adapter에 담는다.
                    if(result.comments?.size != 0)
                    {
                        for ( i in 1..result.comments?.size!!)
                        {
                            commentDTOList.add(Comment(result.comments[i-1].id,result.comments[i-1].author,result.comments[i-1].user_id,result.comments[i-1].comment,
                            result.comments[i-1].content,result.comments[i-1].createdAt))
                        }
                    }

                    mAdapter?.notifyDataSetChanged()

                }else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<LoadPostItem> = gson.getAdapter<LoadPostItem>(
                        LoadPostItem::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
//                            showProgress(false)
                            val result : LoadPostItem = adapter.fromJson(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<LoadPostItem?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })

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
