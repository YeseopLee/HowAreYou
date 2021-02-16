package com.example.howareyou.views.detail

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.*
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.repository.DetailRepository
import com.example.howareyou.util.CoroutineHandler
import com.example.howareyou.util.Event
import dagger.hilt.android.qualifiers.ApplicationContext
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


class DetailViewModel @ViewModelInject constructor(
    private val detailRepository: DetailRepository,
    @ApplicationContext private val context: Context
)  : ViewModel() {

    // posting contents
    var boardName = MutableLiveData<String>()
    var postTitle = MutableLiveData<String>()
    var postContent = MutableLiveData<String>()
    var commentContent = MutableLiveData<String>()
    var commentSize = MutableLiveData<Int>()
    var likeSize = MutableLiveData<Int>()
    var commentArray = MutableLiveData<ArrayList<Comment>>()
    var imageArray = MutableLiveData<ArrayList<ImageDTO>>()
    //comment image uri
    var commentImageUriList : ArrayList<Uri> = arrayListOf()
    var liveCommentImage = MutableLiveData<Uri>()

    // posting info
    lateinit var alarm_id : String
    lateinit var board_id : String

    // ui handler
    val isLoading = ObservableBoolean()
    var commentImageUploaded = MutableLiveData<Boolean>()
    var alarmIsRunning = MutableLiveData<Boolean>()
    private val _moveHome = MutableLiveData<Event<Boolean>>()
    val moveHome: LiveData<Event<Boolean>> = _moveHome
    val commentHint = MutableLiveData<String>()
    var recommentHandler : Boolean = false
    var postLikedisRunning = MutableLiveData<Boolean>()


    init {
        commentHint.value = "댓글을 입력하세요."
        commentImageUploaded.value = false
        alarmIsRunning.value = false
        postLikedisRunning.value = false
        isLoading.set(false)
//        loadPostingContent(board_id)
    }

    fun getValue(board_id: String, alarm_id: String) {
        this.board_id = board_id
        this.alarm_id = alarm_id
        loadPostingContent(board_id)
        getAlarm(App.prefs.myId, board_id)
    }

    fun onRefresh() {
        isLoading.set(true)

        App.prefs.tempCommentId = "none"
        commentHint.value = "댓글을 입력하세요."
        commentArray.value?.clear()
        loadPostingContent(board_id)
        commentArray.notifyObserver()

        isLoading.set(false)
    }

    fun loadPostingContent(board_id: String) {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val postInfo = detailRepository.getPostContent(board_id)
            postContent.value = postInfo.content
            postTitle.value = postInfo.title
            commentSize.value = postInfo.comments?.size
            likeSize.value = postInfo.likeds?.size
            //commentArray.value = postInfo.comments

            var tempList: ArrayList<Comment> = arrayListOf()
            var tempList02 : ArrayList<Comment> = arrayListOf()

            for (i in 0 until postInfo.likeds?.size!!) {
                if (postInfo.likeds[i].user_id == App.prefs.myId) {
                    postLikedisRunning.value = true
                }
            }

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
                if(tempList[i].comment == null) { // comment값이 존재한다는것은 해당 id를 가진 댓글에 달려있는 대댓글이란 소리.
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

            var tempImgArray: ArrayList<ImageDTO> = arrayListOf()
            if (postInfo.image?.isNotEmpty()!!) {
                for (i in 0 until postInfo.image.size) { // 썸네일과 원본 불러오기
                    tempImgArray.add(
                        ImageDTO(
                            ServiceApi.BASE_URL + postInfo.image[i].formats.thumbnail.url,
                            ServiceApi.BASE_URL + postInfo.image[i].url
                        )
                    )
                    Log.e("forloop", i.toString())
                }
                imageArray.value = tempImgArray
            }
            //commentArray.notifyObserver()
        }
    }

    fun post() {
        if (App.prefs.tempCommentId == "none") postComment(
            PostCommentDTO(
                App.prefs.myEmail,
                App.prefs.myName,
                commentContent.value.toString(),
                App.prefs.myId,
                board_id,
                null
            )
        )
        else postComment(
            PostCommentDTO(
                App.prefs.myEmail,
                App.prefs.myName,
                commentContent.value.toString(),
                App.prefs.myId,
                board_id,
                App.prefs.tempCommentId
            )
        )
    }

    fun postComment(data: PostCommentDTO) {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val commentInfo = detailRepository.userComment("Bearer " + App.prefs.myJwt, data)
            var comment_id = commentInfo._id

            if (commentImageUriList.isNotEmpty()) {
                uploadImage(comment_id)
                commentImageUploaded.value = false
            }
            commentContent.value = ""
            commentContent.notifyObserver()
            onRefresh()

        }
    }

    /**
    *  Image Handling
    * */

    fun addImage() {
        TedImagePicker.with(context.applicationContext)
            .start { uri -> showSingleImage(uri) }
    }

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
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val uploadInfo = detailRepository.uploadFile(images, ref, refId, field)
        }
    }

    fun showSingleImage(uri: Uri){
        commentImageUriList.add(uri)
        liveCommentImage.value = commentImageUriList[0]
        commentImageUploaded.value = true // 이미지뷰 보여주고 X 표시 보여줌
    }

    fun controlAlarm(board_id: String) {
        if(alarmIsRunning.value!!) deleteAlarm(board_id)
        else postAlarm(board_id)

    }
    fun postAlarm(board_id: String) { // 알람 받기
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            detailRepository.postAlarm(
                "Bearer " + App.prefs.myJwt, AlarmDTO(
                    App.prefs.myId,
                    board_id
                )
            )
            alarmIsRunning.value = true
        }

        Log.e("TestAlarm","postAlarm")
    }

    fun deleteAlarm(board_id: String) { // 알람 안받기
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            detailRepository.deleteAlarm("Bearer " + App.prefs.myJwt, alarm_id, board_id)
        }
        alarmIsRunning.value = false
        Log.e("TestAlarm","deleteAlarm")
    }

    fun getAlarm(user_id: String, board_id: String){ // 알람받고 있는지 여부 확인
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val alarmInfo = detailRepository.getAlarms()
            for ( i in 0 until alarmInfo.size) {
                if (user_id == alarmInfo[i].user_id && board_id == alarmInfo[i].board._id) { // 이 글의 알람을 받고있는 상태라면
                    alarm_id = alarmInfo[i]._id
                    alarmIsRunning.value = true

                }
            }
        }
    }

    fun deletePost(board_id: String) {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            detailRepository.deletePost("Bearer " + App.prefs.myJwt, board_id, deleteDTO(true))
        } // 실패시 글쓴이가 아님
        // activit 전환
        moveHome()
    }

    fun postLiked(board_id: String) {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {

            try {
                detailRepository.userLiked(
                    PostLikedDTO(
                        App.prefs.myEmail,
                        App.prefs.myId,
                        board_id,
                        null
                    )
                )
                postLikedisRunning.value = true
                likeSize.value = likeSize.value?.plus(1)
            }catch (e : IOException) {

            }
        }
    }

    fun postCommentLiked(comment_id: String) {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            try {
                detailRepository.userLiked(
                    PostLikedDTO(
                        App.prefs.myEmail,
                        App.prefs.myId,
                        null,
                        comment_id
                    )
                )
            }catch (e: IOException) {
                // 토스트 띄우기
            }
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    val builder = AlertDialog.Builder(context.applicationContext).create()


    // activity 전환 트리거
    fun moveHome() {
        _moveHome.value = Event(true)
    }

}





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
