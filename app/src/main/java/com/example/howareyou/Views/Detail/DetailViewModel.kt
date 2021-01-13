package com.example.howareyou.views.detail

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.model.*
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.repository.DetailRepository
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_recomment.view.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class DetailViewModel @ViewModelInject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {

    var boardName = MutableLiveData<String>()
    var postTitle = MutableLiveData<String>()
    var postContent = MutableLiveData<String>()
    var commentSize = MutableLiveData<Int>()
    var likeSize = MutableLiveData<Int>()
    var imageArray = MutableLiveData<ArrayList<ImageDTO>>()
    var commentArray = MutableLiveData<ArrayList<Comment>>()
    var _commentArray = ArrayList<Comment>()


    var commentImageUploaded = MutableLiveData<Boolean>()
    var alarmIsRunning = MutableLiveData<Boolean>()

    lateinit var alarm_id : String
    lateinit var board_id : String

//
//    var commentImageUriList = MutableLiveData<ArrayList<Uri>>()

    //comment image uri
    var commentImageUriList : ArrayList<Uri> = arrayListOf()
    var liveCommentImage = MutableLiveData<Uri>()

    init {
        alarm_id = ""
//        board_id = "5fdb06e9cb3f527e146dc47a"
//        loadPostingContent(board_id)
//        tempinit()
        commentImageUploaded.value = false
        alarmIsRunning.value = false
    }


    fun loadPostingContent(board_id: String) {
        viewModelScope.launch {
            val postInfo = detailRepository.getPostContent(board_id)
            postContent.value = postInfo.content
            postTitle.value = postInfo.title
            commentSize.value = postInfo.comments?.size
            likeSize.value = postInfo.likeds?.size
            //commentArray.value = postInfo.comments

            var tempList: ArrayList<Comment> = arrayListOf()
            var tempList02 : ArrayList<Comment> = arrayListOf()

            if(postInfo.comments?.size != 0) {
//                commentArray.value = postInfo.comments as ArrayList<Comment>?
                for (i in 0 until postInfo.comments?.size!!) {
                    tempList.add(
                        Comment(
                            postInfo.comments[i].id,
                            postInfo.comments[i].author,
                            postInfo.comments[i].user_id,
                            postInfo.comments[i].comment,
                            postInfo.comments[i].content,
                            postInfo.comments[i].createdAt,
                            postInfo.comments[i].image,
                            postInfo.comments[i].likeds
                        )
                    )
                }
            }

            var tempId: String = ""
            for ( i in 0 until tempList.size){
                if(tempList[0].comment == null) { // comment값이 존재한다는것은 해당 id를 가진 댓글에 달려있는 대댓글이란 소리.
                    //commentArray.value?.add(tempList[i])
                    //commentArray.postValue(tempList)
                    tempList02.add(tempList[i])
                    tempId = tempList[i].id
                    for (j in i+1 .. tempList.size) {
                        if (tempList[j - 1].comment == tempId) {
                            tempList02.add(tempList[j - 1])
                        }
                    }
                }
            }

            commentArray.value = tempList02

            Log.e("arrayContentFinsihed", commentArray.value.toString())

//                for (i in 1..tempDTOList.size) {
//                    if (tempDTOList[i - 1].comment == null) { // comment값이 존재하면 해당 id를 가진 댓글에 달려있는 대댓글.
//                        commentDTOList.add(tempDTOList[i - 1])
//                        tempId = tempDTOList[i - 1].id
//                        for (j in i..tempDTOList.size) {
//                            if (tempDTOList[j - 1].comment == tempId) {
//                                commentDTOList.add(tempDTOList[j - 1])
//                            }
//                        }
//                    }

            commentArray.notifyObserver()
        }
    }

//                //LoadPostItem의 comments를 adapter에 연결할 dtolist에 담는다.
//                // 정렬 이전의 임시 list.
//                var tempDTOList: ArrayList<Comment> = arrayListOf()
//
//                if (result.comments?.size != 0) {
//                    for (i in 1..result.comments?.size!!) {
//                        tempDTOList.add(
//                            Comment(
//                                result.comments[i - 1].id,
//                                result.comments[i - 1].author,
//                                result.comments[i - 1].user_id,
//                                result.comments[i - 1].comment,
//                                result.comments[i - 1].content,
//                                result.comments[i - 1].createdAt,
//                                result.comments[i - 1].image,
//                                result.comments[i - 1].likeds
//                            )
//                        )
//                    }
//                }
//
//                //댓글, 대댓글을 상위 id를 가졌는지를 판단하여 정렬, 새로운 list에 담는다.
//                var tempId: String = ""
//                for (i in 1..tempDTOList.size) {
//                    if (tempDTOList[i - 1].comment == null) { // comment값이 존재하면 해당 id를 가진 댓글에 달려있는 대댓글.
//                        commentDTOList.add(tempDTOList[i - 1])
//                        tempId = tempDTOList[i - 1].id
//                        for (j in i..tempDTOList.size) {
//                            if (tempDTOList[j - 1].comment == tempId) {
//                                commentDTOList.add(tempDTOList[j - 1])
//                            }
//                        }
//                    }
//                }

    fun postComment(data: PostCommentDTO) {
        viewModelScope.launch {
            val commentInfo = detailRepository.userComment("Bearer "+App.prefs.myJwt, data)
        }
    }


    /**
    *  Image Handling
    * */

//    fun addImage() {
//        TedImagePicker.with(getApplication())
//            .start { uri -> showSingleImage(uri) }
//    }

    fun uploadImage(comment_id: String) {
        var images = ArrayList<MultipartBody.Part>()
        for (index in 0 until commentImageUriList.size) {
            val file = File(commentImageUriList[index].path)
            val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
            images.add(MultipartBody.Part.createFormData("files", file.name, surveyBody))
        }

        val ref = RequestBody.create(MediaType.parse("text/plain"), "comment")
        val refId = RequestBody.create(MediaType.parse("text/plain"), comment_id)
        val field = RequestBody.create(MediaType.parse("text/plain"), "image")
        viewModelScope.launch {
            val uploadInfo = detailRepository.uploadFile(images, ref, refId, field)
        }
    }

    private fun showSingleImage(uri: Uri){
        commentImageUriList.add(uri)
        liveCommentImage.value = commentImageUriList[0]
        commentImageUploaded.value = true // 이미지뷰 보여주고 X 표시 보여줌
    }

    fun postAlarm(board_id: String) {
        viewModelScope.launch {
            detailRepository.postAlarm("Bearer " + App.prefs.myJwt, AlarmDTO(App.prefs.myId, board_id))
        }
    }

    fun deleteAlarm(board_id: String) {
        viewModelScope.launch {
            detailRepository.deleteAlarm("Bearer " + App.prefs.myJwt, alarm_id, board_id)
        }
    }

    fun getAlarm(user_id: String, board_id: String){
        viewModelScope.launch {
            val alarmInfo = detailRepository.getAlarms()
            for ( i in 0 until alarmInfo.size) {
                if (user_id == alarmInfo[i].user_id && board_id == alarmInfo[i].board._id) {
                    alarm_id = alarmInfo[i]._id
                    alarmIsRunning.value = true

                }
            }
        }
    }

    fun deletePost(board_id: String) {
        viewModelScope.launch {
            detailRepository.deletePost("Bearer " + App.prefs.myJwt, board_id, deleteDTO(true))
        }
    }

    fun postLiked(board_id: String) {
        viewModelScope.launch {
            detailRepository.userLiked(PostLikedDTO(App.prefs.myEmail, App.prefs.myId, board_id, null))
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }


}



//
//// 게시물, 댓글, 대댓글 정보 전부 불러옴
//private fun loadPostingContent(board_id: String) {
//    service?.getPostContent(board_id)?.enqueue(object : Callback<LoadPostItem?> {
//        override fun onResponse(
//            call: Call<LoadPostItem?>?,
//            response: Response<LoadPostItem?>
//
//        ) {
//
//            if (response.isSuccessful) {
//                val result: LoadPostItem = response.body()!!
//                detail_textview_title.text = result.title
//                detail_textview_content.text = result.content
//                detail_textview_comment.text = result.comments?.size.toString()!!
//                detail_textview_liked.text = result.likeds?.size.toString()!!
//
//                // 사용자 좋아요 상태 체크
//                for (i in 1..result.likeds?.size!!) {
//                    if (result.likeds[i - 1].user_id == App.prefs.myId) {
//                        detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
//                    }
//                }
//
//                //LoadPostItem의 comments를 adapter에 연결할 dtolist에 담는다.
//                // 정렬 이전의 임시 list.
//                var tempDTOList: ArrayList<Comment> = arrayListOf()
//
//                if (result.comments?.size != 0) {
//                    for (i in 1..result.comments?.size!!) {
//                        tempDTOList.add(
//                            Comment(
//                                result.comments[i - 1].id,
//                                result.comments[i - 1].author,
//                                result.comments[i - 1].user_id,
//                                result.comments[i - 1].comment,
//                                result.comments[i - 1].content,
//                                result.comments[i - 1].createdAt,
//                                result.comments[i - 1].image,
//                                result.comments[i - 1].likeds
//                            )
//                        )
//                    }
//                }
//
//                //댓글, 대댓글을 상위 id를 가졌는지를 판단하여 정렬, 새로운 list에 담는다.
//                var tempId: String = ""
//                for (i in 1..tempDTOList.size) {
//                    if (tempDTOList[i - 1].comment == null) { // comment값이 존재하면 해당 id를 가진 댓글에 달려있는 대댓글.
//                        commentDTOList.add(tempDTOList[i - 1])
//                        tempId = tempDTOList[i - 1].id
//                        for (j in i..tempDTOList.size) {
//                            if (tempDTOList[j - 1].comment == tempId) {
//                                commentDTOList.add(tempDTOList[j - 1])
//                            }
//                        }
//                    }
//                }
//
//                // 이미지 담기
//                if (result.image?.isNotEmpty()!!) {
//                    for (i in 0 until result.image.size) {
//                        /// 축소된 이미지를 불러온다.
//                        imageList.add(
//                            ImageDTO(
//                                RetrofitClient.BASE_URL + result.image[i].formats.thumbnail.url,
//                                RetrofitClient.BASE_URL + result.image[i].url
//                            )
//                        )
//                    }
//                }
//
//                mCommentAdapter?.notifyDataSetChanged()
//
//            } else {
//                // 실패시 resopnse.errorbody를 객체화
//
//                val gson = Gson()
//                val adapter: TypeAdapter<LoadPostItem> = gson.getAdapter<LoadPostItem>(
//                    LoadPostItem::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        val result: LoadPostItem = adapter.fromJson(
//                            response.errorBody()!!.string()
//                        )
//
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//                finish()
//                Toast.makeText(applicationContext, "불러올 수 없는 글입니다.", Toast.LENGTH_SHORT);
//            }
//
//        }
//
//        override fun onFailure(call: Call<LoadPostItem?>?, t: Throwable) {
//            Log.e("onFailure", t.message!!)
//            finish()
//            Toast.makeText(applicationContext, "불러올 수 없는 글입니다.", Toast.LENGTH_SHORT);
//        }
//    })
//
//}
//
//private fun attemptComment(board_id: String, comment_id: String?){
//    //comment_id에 null이 아닌 값이 들어오면 대댓글
//    detail_edittext_comment.error = null
//    val content: String = detail_edittext_comment.text.toString()
//    var cancel = false
//    var focusView: View? = null
//
//    // 유효성 검사
//    if (content.isEmpty()){
//        detail_edittext_comment.error = "내용을 입력하세요."
//        focusView = detail_edittext_comment
//        cancel = true
//    }
//
//    if(cancel){
//        focusView?.requestFocus()
//    } else {
//        detail_edittext_comment.text = null
//        postComment(
//            PostCommentDTO(
//                App.prefs.myEmail,
//                App.prefs.myName,
//                content,
//                App.prefs.myId,
//                board_id,
//                comment_id
//            )
//        )
//        focusView = null
//    }
//}
//
//private fun postComment(data: PostCommentDTO) {
//    service?.userComment("Bearer " + App.prefs.myJwt, data)?.enqueue(object :
//        Callback<PostCommentResponseDTO?> {
//        override fun onResponse(
//            call: Call<PostCommentResponseDTO?>?,
//            response: Response<PostCommentResponseDTO?>
//
//        ) {
//
//            if (response.isSuccessful) {
//                val result: PostCommentResponseDTO = response.body()!!
//                var comment_id = result._id
//                if (commentImageUriList.isNotEmpty()) {
//                    uploadImage(comment_id) // 이미지가 업로드 되었다면 이미지 post
//                    detail_imageview_commentimage.visibility = View.GONE
//                } else {
//                    onRefresh()
//                }
//
//            } else {
//                // 실패시 resopnse.errorbody를 객체화
//                val gson = Gson()
//                val adapter: TypeAdapter<PostCommentResponseDTO> =
//                    gson.getAdapter<PostCommentResponseDTO>(
//                        PostCommentResponseDTO::class.java
//                    )
//                try {
//                    if (response.errorBody() != null) {
//                        val result: PostCommentResponseDTO = adapter.fromJson(
//                            response.errorBody()!!.string()
//                        )
//                        System.out.println(response.errorBody().toString())
//                        System.out.println(response.errorBody()!!.string())
//
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//        }
//
//        override fun onFailure(call: Call<PostCommentResponseDTO?>?, t: Throwable) {
//            Log.e("onFailure", t.message!!)
//        }
//    })
//
//}
//
//private fun uploadImage(comment_id: String){
//    var images = ArrayList<MultipartBody.Part>()
//    for (index in 0 until commentImageUriList.size) {
//        val file = File(commentImageUriList[index].path)
//        val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
//        images.add(MultipartBody.Part.createFormData("files", file.name, surveyBody))
//    }
//
//    val ref = RequestBody.create(MediaType.parse("text/plain"), "comment")
//    val refId = RequestBody.create(MediaType.parse("text/plain"), comment_id)
//    val field = RequestBody.create(MediaType.parse("text/plain"), "image")
//
//
//    service?.uploadFile(images, ref, refId, field)?.enqueue(object :
//        Callback<UploadImageResponseDTO?> {
//        override fun onResponse(
//            call: Call<UploadImageResponseDTO?>,
//            response: Response<UploadImageResponseDTO?>
//        ) {
//            var result: UploadImageResponseDTO = response.body()!!
//            onRefresh()
//        }
//
//        override fun onFailure(call: Call<UploadImageResponseDTO?>, t: Throwable) {
//            Log.d("onFailure", t.message)
//        }
//
//    })
//}
//
//private fun postLiked(data: PostLikedDTO) {
//    service?.userLiked(data)?.enqueue(object : Callback<PostingResponseDTO?> {
//        override fun onResponse(
//            call: Call<PostingResponseDTO?>?,
//            response: Response<PostingResponseDTO?>
//
//        ) {
//            detail_button_liked.setBackgroundResource(R.drawable.ic_thumbsup)
//            detail_textview_liked.text =
//                (detail_textview_liked.text.toString().toInt() + 1).toString()
//
//        }
//
//        override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
//            Toast.makeText(applicationContext, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
//            Log.e("onFailure", t.message!!)
//        }
//    })
//
//}
//
//private fun deletePosting(board_id: String) {
//    service?.deletePost("Bearer " + App.prefs.myJwt, board_id, deleteDTO(true))?.enqueue(object :
//        Callback<Void> {
//        override fun onResponse(
//            call: Call<Void>?,
//            response: Response<Void>
//
//        ) {
//
//            if (response.isSuccessful) {
//                finish()
//            } else {
//                // 실패시 resopnse.errorbody를 객체화
//                Toast.makeText(applicationContext, "글쓴이가 아닙니다.", Toast.LENGTH_SHORT).show()
//                val gson = Gson()
//                val adapter: TypeAdapter<LoadPostItem> = gson.getAdapter<LoadPostItem>(
//                    LoadPostItem::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        val result: LoadPostItem = adapter.fromJson(
//                            response.errorBody()!!.string()
//                        )
//
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//        }
//
//        override fun onFailure(call: Call<Void>?, t: Throwable) {
//            Log.e("onFailure", t.message!!)
//        }
//    })
//
//}
//
//private fun getAlarm(user_id: String, board_id: String) {
//    service?.getAlarms()?.enqueue(object : Callback<AlarmResponseDTO?> {
//        override fun onResponse(
//            call: Call<AlarmResponseDTO?>?,
//            response: Response<AlarmResponseDTO?>
//        ) {
//            if (response.isSuccessful) {
//                var result: AlarmResponseDTO = response.body()!!
//                for (i in 0 until result.size) {
//                    if (user_id == result[i].user_id && board_id == result[i].board._id) {
//                        // 해당 글에 대한 알람을 받는상태.
//                        alarm_id = result[i]._id
//                        detail_button_notification.setBackgroundResource(R.drawable.ic_notification)
//                        alarmisRunning = true
//                    } else {
//                    }
//                }
//                if (!alarmisRunning) detail_button_notification.setBackgroundResource(R.drawable.ic_notification_gray)
//            }
//        }
//
//        override fun onFailure(call: Call<AlarmResponseDTO?>?, t: Throwable) {
//            Log.e("onFailure", t.message!!)
//        }
//    })
//}
//
//private fun deleteAlarm(board_id: String) {
//    service?.deleteAlarm("Bearer " + App.prefs.myJwt, alarm_id, board_id)?.enqueue(object :
//        Callback<Void?> {
//        override fun onResponse(
//            call: Call<Void?>?,
//            response: Response<Void?>
//        ) {
//            if (response.isSuccessful) {
//                Log.d("onSuccess", alarm_id)
//                alarmisRunning = false
//            }
//        }
//
//        override fun onFailure(call: Call<Void?>?, t: Throwable) {
//            Log.e("onFailure", t.message!!)
//        }
//    })
//}
//
//private fun postAlarm(board_id: String) {
//    service?.postAlarm("Bearer " + App.prefs.myJwt, AlarmDTO(App.prefs.myId, board_id))?.enqueue(
//        object : Callback<Void?> {
//            override fun onResponse(
//                call: Call<Void?>?,
//                response: Response<Void?>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("onSuccess", alarm_id)
//                    alarmisRunning = true
//                }
//            }
//
//            override fun onFailure(call: Call<Void?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//}
