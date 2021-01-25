package com.example.howareyou.views.writing

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.model.PostingDTO
import com.example.howareyou.repository.WritingRepository
import com.example.howareyou.util.CoroutineHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class WritingViewModel @ViewModelInject constructor(
    private val writingRepository: WritingRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    lateinit var board_id : String
    var uriList = MutableLiveData<ArrayList<Uri>>()
    var _uriList = ArrayList<Uri>()

    val title = MutableLiveData<String>()
    var _title = String
    val content = MutableLiveData<String>()
    var _content = String

    init {

    }

    fun addImage() {
        TedImagePicker.with(context.applicationContext)
            .max(5-_uriList.size, R.string.ted_image_picker_max_count)
            .startMultiImage { uriList -> showMultiImage(uriList) }
    }

    private fun showMultiImage(uriList: List<Uri>) {
        for (element in uriList)
        {
            _uriList.add(element)
        }
        Log.e("uriList",this.uriList.value.toString())
        this.uriList.value = _uriList
    }

    fun attemptPost() {

        Log.e("attemptPost","attempt")
        userPost(PostingDTO(App.prefs.myEmail,App.prefs.myId,App.prefs.myName,title.value!!.toString(),content.value!!.toString(),"header",App.prefs.myCode))
    }

    fun userPost(data: PostingDTO){
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            Log.e("attemptPost22","attempt")
            try {
                if(data.code == "" || data.code == "code") {
                    Log.e("attemptPost223","attempt")
                    Toast.makeText(context.applicationContext, "게시판을 선택하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("attemptPost225","attempt")
                    Log.e("TestCodeWhat2",data.code)
                    Log.e("TestCodeWhat3",App.prefs.myCode)
                    val postInfo = writingRepository.userPost("Bearer " + App.prefs.myJwt, data)
                    board_id = postInfo._id
                    if(_uriList.isNotEmpty()) {
                        uploadImage(board_id)
                        Log.e("attemptPost224","attempt")

                    } else {}
                }
            } catch (e : IOException) {

            }
        }
    }

    private fun uploadImage(board_id: String){

        var images = ArrayList<MultipartBody.Part>()
        for (index in 0 until _uriList.size) {
            val file = File(_uriList[index].path)
            val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
            images.add(MultipartBody.Part.createFormData("files",file.name,surveyBody))
        }

        val ref = RequestBody.create(MediaType.parse("text/plain"),"board")
        val refId = RequestBody.create(MediaType.parse("text/plain"),board_id)
        val field = RequestBody.create(MediaType.parse("text/plain"),"image")

        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val imageInfo = writingRepository.uploadFile(images,ref, refId, field)
        }

    }


}




//private fun startPost(data: PostingDTO) {
//    service?.userPost("Bearer "+ App.prefs.myJwt,data)?.enqueue(object :
//        Callback<PostingResponseDTO?> {
//        override fun onResponse(
//            call: Call<PostingResponseDTO?>?,
//            response: Response<PostingResponseDTO?>
//
//        ) {
//            if(response.isSuccessful)
//            {
//                val result: PostingResponseDTO = response.body()!!
//                board_id = result._id
//                if(_uriList.isNotEmpty())
//                {
//                    uploadImage(result._id) // 이미지를 올렸다면 이미지 post를 호출한다.
//                }
//                else {
//                    val intent = Intent(applicationContext, DetailActivity::class.java)
//                    intent.putExtra("board_id",board_id)
//                    startActivity(intent)
//                    finish()
//                }
//
//            }else {
//                // 실패시 resopnse.errorbody를 객체화
//                val gson = Gson()
//                val adapter: TypeAdapter<PostingResponseDTO> = gson.getAdapter<PostingResponseDTO>(
//                    PostingResponseDTO::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        showProgress(false)
//                        val result : PostingResponseDTO = adapter.fromJson(response.errorBody()!!.string())
//                        System.out.println(result.message)
//
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//        }
//
//        override fun onFailure(call: Call<PostingResponseDTO?>?, t: Throwable) {
//            Log.e("error", t.message!!)
//        }
//    })
//}
