package com.example.howareyou

import android.content.Context
import android.graphics.Rect
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.example.howareyou.Model.*
import com.example.howareyou.Util.App
import com.example.howareyou.Util.OnSingleClickListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_writing.*
import kotlinx.android.synthetic.main.item_imageshow.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException


class DetailActivity : AppCompatActivity() {

    private var service: ServiceApi? = null

    //comment recyclerview data / adapter
    var commentDTOList : ArrayList<Comment> = arrayListOf()
    var mAdapter = DetailAdapter(this, commentDTOList)

    //image recyclerview data / adapter
    var imageList : ArrayList<ImageDTO> = arrayListOf()
    var mImageAdapter = Detail_imageAdapter(this, imageList)

    //commnet image uri
    var commentImageUriList : ArrayList<Uri> = arrayListOf()

    //alarm check
    var alarmisRunning : Boolean = false
    var alarm_id : String = ""

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

        detail_recyclerview_imageview.adapter = mImageAdapter
        val lm2 = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        detail_recyclerview_imageview.layoutManager = lm2
        detail_recyclerview_imageview.setHasFixedSize(true)

        getAlarm(App.prefs.myId,board_id)
        System.out.println("현재id"+App.prefs.myId)
        System.out.println("현재board"+board_id)

        // buttons
        detail_button_back.setOnClickListener { //뒤로가기 버튼
            //finish()
        }

        detail_button_postcomment.setOnClickListener { // 댓글등록 버튼
            if(App.prefs.tempCommentId == "none") attemptComment(board_id, null)
            else attemptComment(board_id, App.prefs.tempCommentId)
            mAdapter.notifyDataSetChanged()

        }

        detail_button_addphoto.setOnClickListener { // 댓글 이미지등록
            TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }

        }

        detail_button_liked.setOnClickListener(object : OnSingleClickListener() { // 좋아요버튼
            override fun onSingleClick(view: View) {
                //게시물 or 코멘트 좋아요
                postLiked(PostLikedDTO(App.prefs.myEmail, App.prefs.myId, board_id, null));
            }
        })

        //알람버튼
        detail_button_notification.setOnClickListener(object : OnSingleClickListener(){ //알람버튼
            override fun onSingleClick(view: View) {
                if(alarmisRunning) {
                    deleteAlarm(board_id)
                    detail_button_notification.setBackgroundResource(R.drawable.ic_notification_gray)
                    Toast.makeText(applicationContext,"댓글 알림을 받지 않습니다.",Toast.LENGTH_SHORT).show()
                }
                else {
                    postAlarm(board_id)
                    detail_button_notification.setBackgroundResource(R.drawable.ic_notification)
                    Toast.makeText(applicationContext,"댓글 알림을 받습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        })

        // alert dialog value
        val builder = AlertDialog.Builder(this).create()
        detail_button_morevert.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(view: View) {

                val dialogView = layoutInflater.inflate(R.layout.activity_more_menu, null)
                val BtnReport = dialogView.findViewById<Button>(R.id.moremenu_button_report)
                val BtnRecomment = dialogView.findViewById<Button>(R.id.moremenu_button_recomment)
                val BtnDelete = dialogView.findViewById<Button>(R.id.moremenu_button_delete)
                BtnRecomment.visibility = View.GONE

                builder.setView(dialogView)
                builder.show()

                BtnReport.setOnClickListener {
                    builder.dismiss()
                }

                BtnDelete.setOnClickListener {
                    deletePosting(board_id)
                    builder.dismiss()
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 대댓글 비활성화
        App.prefs.tempCommentId = "none"
        mAdapter.notifyDataSetChanged()
    }

    private fun showSingleImage(uri : Uri){
        commentImageUriList.add(uri)
        Glide.with(this).load(commentImageUriList[0]).into(detail_imageview_commentimage)
        detail_imageview_commentimage.visibility = View.VISIBLE
        detail_button_deleteCommentimage.visibility = View.VISIBLE
    }

    // 게시물, 댓글, 대댓글 정보 전부 불러옴
    private fun loadPostingContent(board_id: String) {
        showProgress(true);
        service?.getPostContent(board_id)?.enqueue(object : Callback<LoadPostItem?> {
            override fun onResponse(
                call: Call<LoadPostItem?>?,
                response: Response<LoadPostItem?>

            ) {

                if (response.isSuccessful) {
                    showProgress(false);
                    val result: LoadPostItem = response.body()!!
                    detail_textview_title.text = result.title
                    detail_textview_content.text = result.content
                    detail_textview_comment.text = result.comments?.size.toString()!!
                    detail_textview_liked.text = result.likeds?.size.toString()!!

                    // 사용자 좋아요 상태 체크
                    for (i in 1..result.likeds?.size!!) {
                        if (result.likeds[i - 1].user_id == App.prefs.myId) {
                            detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
                        }
                    }

                    //LoadPostItem의 comments를 adapter에 연결할 dtolist에 담는다.
                    // 정렬 이전의 임시 list.
                    var tempDTOList: ArrayList<Comment> = arrayListOf()

                    if (result.comments?.size != 0) {
                        for (i in 1..result.comments?.size!!) {
                            tempDTOList.add(
                                Comment(
                                    result.comments[i - 1].id,
                                    result.comments[i - 1].author,
                                    result.comments[i - 1].user_id,
                                    result.comments[i - 1].comment,
                                    result.comments[i - 1].content,
                                    result.comments[i - 1].createdAt,
                                    result.comments[i - 1].image,
                                    result.comments[i - 1].likeds
                                )
                            )
                        }
                    }

                    //댓글, 대댓글을 상위 id를 가졌는지를 판단하여 정렬, 새로운 list에 담는다.
                    var tempId: String = ""
                    for (i in 1..tempDTOList.size) {
                        if (tempDTOList[i - 1].comment == null) { // comment값이 존재하면 해당 id를 가진 댓글에 달려있는 대댓글.
                            commentDTOList.add(tempDTOList[i - 1])
                            tempId = tempDTOList[i - 1].id
                            for (j in i..tempDTOList.size) {
                                if (tempDTOList[j - 1].comment == tempId) {
                                    commentDTOList.add(tempDTOList[j - 1])
                                }
                            }
                        }
                    }

                    // 이미지 담기
                    if(result.image?.isNotEmpty()!!)
                    {
                        for (i in 0 until result.image.size)
                        {
                            /// 축소된 이미지를 불러온다.
                            imageList.add(ImageDTO(RetrofitClient.BASE_URL+result.image[i].formats.thumbnail.url,RetrofitClient.BASE_URL+result.image[i].url))
                        }
                    }

                    mAdapter?.notifyDataSetChanged()

                } else {
                    // 실패시 resopnse.errorbody를 객체화

                    val gson = Gson()
                    val adapter: TypeAdapter<LoadPostItem> = gson.getAdapter<LoadPostItem>(
                        LoadPostItem::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: LoadPostItem = adapter.fromJson(
                                response.errorBody()!!.string()
                            )

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    finish()
                    Toast.makeText(applicationContext,"불러올 수 없는 글입니다.",Toast.LENGTH_SHORT);
                }

            }

            override fun onFailure(call: Call<LoadPostItem?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
                finish()
                Toast.makeText(applicationContext,"불러올 수 없는 글입니다.",Toast.LENGTH_SHORT);
            }
        })

    }

    private fun attemptComment(board_id: String, comment_id: String?){
        //comment_id에 null이 아닌 값이 들어오면 대댓글
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
            postComment(
                PostCommentDTO(
                    App.prefs.myEmail,
                    App.prefs.myName,
                    content,
                    App.prefs.myId,
                    board_id,
                    comment_id
                )
            )
            focusView = null
        }
    }

    private fun postComment(data: PostCommentDTO) {
        service?.userComment("Bearer " + App.prefs.myJwt, data)?.enqueue(object :
            Callback<PostCommentResponseDTO?> {
            override fun onResponse(
                call: Call<PostCommentResponseDTO?>?,
                response: Response<PostCommentResponseDTO?>

            ) {

                if (response.isSuccessful) {
                    val result: PostCommentResponseDTO = response.body()!!
                    System.out.println("왜안돼")
                    var comment_id = result._id
                    if(commentImageUriList.isNotEmpty()) uploadImage(comment_id)
                    mAdapter?.notifyDataSetChanged()

                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    System.out.println("왜안돼2")
                    val gson = Gson()
                    val adapter: TypeAdapter<PostCommentResponseDTO> = gson.getAdapter<PostCommentResponseDTO>(
                        PostCommentResponseDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: PostCommentResponseDTO = adapter.fromJson(
                                response.errorBody()!!.string()
                            )
                            System.out.println(response.errorBody().toString())
                            System.out.println(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                // 댓글 등록 후 새로고침
                val intent = intent
                finish()
                startActivity(intent)

            }

            override fun onFailure(call: Call<PostCommentResponseDTO?>?, t: Throwable) {
                System.out.println("왜안돼3")
                Log.e("onFailure", t.message!!)
            }
        })

    }

    private fun uploadImage(comment_id: String){
        var images = ArrayList<MultipartBody.Part>()
        for (index in 0 until commentImageUriList.size) {
            val file = File(commentImageUriList[index].path)
            val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
            images.add(MultipartBody.Part.createFormData("files",file.name,surveyBody))
        }

        val ref = RequestBody.create(MediaType.parse("text/plain"),"comment")
        val refId = RequestBody.create(MediaType.parse("text/plain"),comment_id)
        val field = RequestBody.create(MediaType.parse("text/plain"),"image")


        service?.uploadFile(images,ref,refId,field)?.enqueue(object : Callback<UploadImageResponseDTO?> {
            override fun onResponse(
                call: Call<UploadImageResponseDTO?>,
                response: Response<UploadImageResponseDTO?>
            ) {
                var result : UploadImageResponseDTO = response.body()!!
                for (index in 0 until result.size){
                    System.out.println("test"+result[index]._id)
                }
            }

            override fun onFailure(call: Call<UploadImageResponseDTO?>, t: Throwable) {
                System.out.println("fail")
                Log.d("??",t.message)
            }

        })
    }

    private fun postLiked(data: PostLikedDTO) {
        service?.userLiked(data)?.enqueue(object : Callback<PostingResponseDTO?> {
            override fun onResponse(
                call: Call<PostingResponseDTO?>?,
                response: Response<PostingResponseDTO?>

            ) {
                detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
                detail_textview_liked.text = (detail_textview_liked.text.toString().toInt() + 1).toString()

            }

            override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
                Toast.makeText(applicationContext, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
                Log.e("onFailure", t.message!!)
            }
        })

    }

    private fun deletePosting(board_id: String) {
        service?.deletePost("Bearer " + App.prefs.myJwt, board_id)?.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>?,
                response: Response<Void>

            ) {

                if (response.isSuccessful) {
                    finish()
                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    Toast.makeText(applicationContext,"글쓴이가 아닙니다.",Toast.LENGTH_SHORT).show()
                    val gson = Gson()
                    val adapter: TypeAdapter<LoadPostItem> = gson.getAdapter<LoadPostItem>(
                        LoadPostItem::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: LoadPostItem = adapter.fromJson(
                                response.errorBody()!!.string()
                            )

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<Void>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })

    }

    private fun getAlarm(user_id : String, board_id: String) {
        service?.getAlarms()?.enqueue(object : Callback<AlarmResponseDTO?> {
            override fun onResponse(
                call: Call<AlarmResponseDTO?>?,
                response: Response<AlarmResponseDTO?>
            ) {
                if (response.isSuccessful) {
                    var result : AlarmResponseDTO = response.body()!!
                    for (i in 0 until result.size){
                        if(user_id == result[i].user_id && board_id == result[i].board._id ){
                            // 해당 글에 대한 알람을 받는상태.
                            alarm_id = result[i]._id
                            detail_button_notification.setBackgroundResource(R.drawable.ic_notification)
                            alarmisRunning = true
                        } else {
                        }
                    }
                    if(!alarmisRunning) detail_button_notification.setBackgroundResource(R.drawable.ic_notification_gray)
                }
            }

            override fun onFailure(call: Call<AlarmResponseDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })
    }

    private fun deleteAlarm(board_id: String) {
        service?.deleteAlarm("Bearer "+App.prefs.myJwt,alarm_id,board_id)?.enqueue(object : Callback<Void?> {
            override fun onResponse(
                call: Call<Void?>?,
                response: Response<Void?>
            ) {
                if (response.isSuccessful) {
                    Log.d("onSuccess",alarm_id)
                    alarmisRunning = false
                }
            }

            override fun onFailure(call: Call<Void?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })
    }

    private fun postAlarm(board_id: String) {
        service?.postAlarm("Bearer "+App.prefs.myJwt, AlarmDTO(App.prefs.myId,board_id))?.enqueue(object : Callback<Void?> {
            override fun onResponse(
                call: Call<Void?>?,
                response: Response<Void?>
            ) {
                if (response.isSuccessful) {
                    Log.d("onSuccess",alarm_id)
                    alarmisRunning = true
                }
            }

            override fun onFailure(call: Call<Void?>?, t: Throwable) {
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
