package com.example.howareyou

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.*
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList


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

        // buttons
        detail_button_back.setOnClickListener {
            finish()
        }

        detail_button_postcomment.setOnClickListener {
            attemptComment(board_id,App.prefs.tempCommentId)
        }

        detail_button_liked.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(view: View) {
                postLiked(PostLikedDTO(App.prefs.myEmail,App.prefs.myId,board_id,null));
            }
        })

        // alert dialog value
        val builder = AlertDialog.Builder(this).create()

        detail_button_morevert.setOnClickListener (object : OnSingleClickListener(){
            override fun onSingleClick(view: View) {

                val dialogView = layoutInflater.inflate(R.layout.activity_more_menu, null)
                val BtnReport = dialogView.findViewById<Button>(R.id.moremenu_button_report)
                val BtnRecomment = dialogView.findViewById<Button>(R.id.moremenu_button_recomment)
                BtnRecomment.visibility = View.GONE

                builder.setView(dialogView)
                builder.show()

                BtnReport.setOnClickListener {
                    builder.dismiss()
                }

            }
        })


        // Get the LayoutInflater from Context
        val layoutInflater:LayoutInflater = LayoutInflater.from(applicationContext)

        // Inflate the layout using LayoutInflater
        val view: View = layoutInflater.inflate(
            R.layout.item_comment, // Custom view/ layout
            null, // Root layout to attach the view
            true // Attach with root layout or not
        )


    }

    override fun onResume() {
        super.onResume()
        App.prefs.tempCommentId = null.toString()
    }

    private fun loadPostingContent(board_id : String) {
        showProgress(true);
        service?.getPostContent(board_id)?.enqueue(object : Callback<LoadPostItem?> {
            override fun onResponse(
                call: Call<LoadPostItem?>?,
                response: Response<LoadPostItem?>

            ) {

                if(response.isSuccessful)
                {
                    showProgress(false);
                    val result: LoadPostItem = response.body()!!
                    detail_textview_title.text = result.title
                    detail_textview_content.text = result.content
                    detail_textview_comment.text = result.comments?.size.toString()!!
                    detail_textview_liked.text = result.likeds?.size.toString()!!

                    // 사용자 좋아요 상태 체크
                    for (i in 1..result.likeds?.size!!){
                        if(result.likeds[i-1].user_id == App.prefs.myId){
                            detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
                        }
                    }

                    //LoadPostItem의 comments를 adapter에 연결할 dtolist에 담는다.
                    // 정렬 이전의 임시 list
                    var tempDTOList : ArrayList<Comment> = arrayListOf()

                    if(result.comments?.size != 0)
                    {
                        for ( i in 1..result.comments?.size!!)
                        {
                            tempDTOList.add(Comment(result.comments[i-1].id,result.comments[i-1].author,result.comments[i-1].user_id,result.comments[i-1].comment,
                            result.comments[i-1].content,result.comments[i-1].createdAt))
                        }
                    }

                    //댓글, 대댓글을 id로 정렬하여 새로운 list에 담는다.
                    var tempId: String = ""
                    for ( i in 1..tempDTOList.size)
                    {
                        if(tempDTOList[i-1].comment == null)
                        {
                            commentDTOList.add(tempDTOList[i-1])
                            tempId = tempDTOList[i-1].id
                            for ( j in i..tempDTOList.size)
                            {
                                if (tempDTOList[j-1].comment == tempId)
                                {
                                    commentDTOList.add(tempDTOList[j-1])
                                }
                            }
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
                            val result : LoadPostItem = adapter.fromJson(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<LoadPostItem?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
                showProgress(false);
            }
        })

    }

    private fun attemptComment(board_id: String, comment_id: String?){
        detail_edittext_comment.error = null
        val content: String = detail_edittext_comment.text.toString()
        var cancel = false
        var focusView: View? = null

        // 유효성 검사
        if (content.isEmpty()){
            detail_edittext_comment.error = "내용을 입력하세요."
            focusView = detail_edittext_comment
            cancel = true
        }

        if(cancel){
            focusView?.requestFocus()
        } else {
            detail_edittext_comment.text = null
            postComment(PostCommentDTO(App.prefs.myEmail,App.prefs.myName,content,App.prefs.myId,board_id,comment_id))
            focusView = null
        }
    }

    private fun postComment(data: PostCommentDTO) {
        service?.userComment("Bearer "+App.prefs.myJwt,data)?.enqueue(object : Callback<PostCommentDTO?> {
            override fun onResponse(
                call: Call<PostCommentDTO?>?,
                response: Response<PostCommentDTO?>

            ) {

                if(response.isSuccessful)
                {
                    val result: PostCommentDTO = response.body()!!
                    mAdapter?.notifyDataSetChanged()

                }else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<PostCommentDTO> = gson.getAdapter<PostCommentDTO>(
                        PostCommentDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result : PostCommentDTO = adapter.fromJson(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<PostCommentDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })

    }

    private fun postLiked(data: PostLikedDTO) {
        service?.userLiked(data)?.enqueue(object : Callback<PostingResponseDTO?> {
            override fun onResponse(
                call: Call<PostingResponseDTO?>?,
                response: Response<PostingResponseDTO?>

            ) {}

            override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
                Toast.makeText(applicationContext,"이미 좋아요를 눌렀습니다.",Toast.LENGTH_SHORT).show()
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

    private fun showProgress(show: Boolean){
        detail_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
    }
}
