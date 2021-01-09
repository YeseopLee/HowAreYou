package com.example.howareyou.views.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.Util.Event
import com.example.howareyou.model.*
import com.example.howareyou.repository.HomeRepository
import kotlinx.coroutines.launch


class HomePagerViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    lateinit var last_id: String

    var postArray = MutableLiveData<ArrayList<LoadPostItem>>()

    init {

        loadPost()
    }

    fun loadPost(){

        viewModelScope.launch {
            val postInfo = homeRepository.getAllPost("Bearer " + App.prefs.myJwt)
            postArray.value = postInfo
            last_id = postInfo[postInfo.size-1].id
        }
    }

    fun loadPostMore() {

        viewModelScope.launch {
            val postInfo = homeRepository.getAllPostMore("Bearer "+ App.prefs.myJwt,last_id,30)
            for ( i in 0 until postInfo.size){
                postArray.value?.add(postInfo[i])
            }
            if(postInfo.size > 0) last_id = postInfo[postInfo.size-1].id
            postArray.notifyObserver()
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
//
//    private fun loadPosting() {
//        service?.getAllPost("Bearer " + App.prefs.myJwt)?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if (response.isSuccessful) {
//                    showProgress(false)
//                    val result: LoadPostDTO = response.body()!!
//                    val postSize: Int = result.size - 1
//
//                    if (result.size != 0) {
//                        for (i in 0..postSize) {
//
//                            if(!result[i].is_deleted)
//                            {
//
//                                postingDTOlist?.add(
//                                    LoadPostItem(
//                                        result[i].id,
//                                        result[i].title,
//                                        result[i].content,
//                                        result[i].author,
//                                        result[i].code,
//                                        result[i].comments,
//                                        result[i].likeds,
//                                        result[i].viewed,
//                                        result[i].createdAt,
//                                        result[i].header,
//                                        result[i].user_id,
//                                        result[i].is_deleted,
//                                        result[i].image
//                                    )
//                                )
//                            }
//                            lastboard_id = result[i].id
//
//                        }
//
//                        //
//                        homeAdapter.notifyDataSetChanged()
//
//                    } else {
//                        //TODO
//                    }
//
//                } else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<StatuscodeResponse> =
//                        gson.getAdapter<StatuscodeResponse>(
//                            StatuscodeResponse::class.java
//                        )
//                    try {
//                        if (response.errorBody() != null) {
//                            val result: StatuscodeResponse = adapter.fromJson(
//                                response.errorBody()!!.string()
//                            )
//                            if (result.statusCode == 401) // jwt 토큰 만료
//                            {
//
//                            }
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//                showProgress(false)
//            }
//        })
//
//    }

    fun moveMainPage() {
        _moveMainPage.value = Event(true)
    }


}