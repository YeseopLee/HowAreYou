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
    var imageList : ArrayList<String> = arrayListOf()
    var mImageAdapter = Detail_imageAdapter(this, imageList)

    //commnet image uri
    var commentImageUri : Uri? = null

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

        // buttons
        detail_button_back.setOnClickListener { //뒤로가기 버튼
            //finish()
        }


        detail_button_postcomment.setOnClickListener { // 댓글등록 버튼
            if(App.prefs.tempCommentId == "none") attemptComment(board_id, null)
            else attemptComment(board_id, App.prefs.tempCommentId)

            val intent = intent
            finish()
            startActivity(intent)
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
        App.prefs.tempCommentId = "none"
        mAdapter.notifyDataSetChanged()
    }

    private fun showSingleImage(uri : Uri){
        commentImageUri = uri
        Glide.with(this).load(commentImageUri).into(detail_imageview_commentimage)
        detail_imageview_commentimage.visibility = View.VISIBLE
        detail_button_deleteCommentimage.visibility = View.VISIBLE
    }

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
                    // 정렬 이전의 임시 list
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
                                    result.comments[i - 1].createdAt
                                )
                            )
                        }
                    }

                    //댓글, 대댓글을 id로 정렬하여 새로운 list에 담는다.
                    var tempId: String = ""
                    for (i in 1..tempDTOList.size) {
                        if (tempDTOList[i - 1].comment == null) {
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

                            ////////////// 서버주소 해결해야함
                            imageList.add("http://211.208.220.233:1337"+result.image[i].url)
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
            Callback<PostCommentDTO?> {
            override fun onResponse(
                call: Call<PostCommentDTO?>?,
                response: Response<PostCommentDTO?>

            ) {

                if (response.isSuccessful) {
                    val result: PostCommentDTO = response.body()!!
                    //var comment_id = result.comment.id
                    if(commentImageUri != null) //TODO 이미지 업로드 포스트
                    mAdapter?.notifyDataSetChanged()

                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<PostCommentDTO> = gson.getAdapter<PostCommentDTO>(
                        PostCommentDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: PostCommentDTO = adapter.fromJson(
                                response.errorBody()!!.string()
                            )

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

    private fun uploadImage(comment_id: String){
//        var file = File(uriList22[0].path)
//        var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"),file)
//        var body : MultipartBody.Part = MultipartBody.Part.createFormData("files",file.name,requestBody)
//

        var images = ArrayList<MultipartBody.Part>()
        for (index in 0 until _uriList.size) {
            val file = File(_uriList[index].path)
            val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
            images.add(MultipartBody.Part.createFormData("files",file.name,surveyBody))
        }

        val ref = RequestBody.create(MediaType.parse("text/plain"),"board")
        val refId = RequestBody.create(MediaType.parse("text/plain"),board_id)
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

//    private fun getImage(data: String) {
//        service?.getImage(data)?.enqueue(object : Callback<imageItem?> {
//            override fun onResponse(
//                call: Call<imageItem?>?,
//                response: Response<imageItem?>
//
//            ) {
//
//
//            }
//
//            override fun onFailure(call: Call<UploadImageResponseDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }

    private fun showImages(){

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
