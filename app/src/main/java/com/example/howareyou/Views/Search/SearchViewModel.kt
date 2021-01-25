package com.example.howareyou.views.search

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.LoadPostDTO
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.repository.SearchRepository
import com.example.howareyou.util.CoroutineHandler
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    lateinit var last_id: String
    val isLoading = ObservableBoolean()
    lateinit var target : String
    var postArray = MutableLiveData<ArrayList<LoadPostItem>>()
    var _postArray = ArrayList<LoadPostItem>()


    init {
//        postArray.value = LoadPostDTO()
//        postArray.value?.clear()
    }

    fun getValue(target: String) {
        this.target = target
    }

    fun searchPosting() {
        Log.e("searched","searched")
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val searchInfo = searchRepository.getSearchPost()
            for (i in 0 until searchInfo.size) {
                if(searchInfo[i].title == target || searchInfo[i].content.contains(target)) {
                    if(!searchInfo[i].is_deleted) { // 삭제한글은 안불러옴
                        _postArray.add(LoadPostItem(
                            searchInfo[i].id,
                            searchInfo[i].title,
                            searchInfo[i].content,
                            searchInfo[i].author,
                            searchInfo[i].code,
                            searchInfo[i].comments,
                            searchInfo[i].likeds,
                            searchInfo[i].viewed,
                            searchInfo[i].createdAt,
                            searchInfo[i].header,
                            searchInfo[i].user_id,
                            searchInfo[i].is_deleted,
                            searchInfo[i].image))
                        if (searchInfo.size >0 ) last_id = searchInfo[i].id
                    }
                }

            }

            postArray.value = _postArray


        }
    }

    fun searchPostingMore() {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val searchInfo = searchRepository.getSearchPostMore("Bearer "+App.prefs.myJwt, last_id, 100)
            for (i in 0 until searchInfo.size) {
                if(searchInfo[i].title == target || searchInfo[i].content.contains(target)) {
                    if(!searchInfo[i].is_deleted) { // 삭제한글은 안불러옴
                        _postArray.add(LoadPostItem(
                            searchInfo[i].id,
                            searchInfo[i].title,
                            searchInfo[i].content,
                            searchInfo[i].author,
                            searchInfo[i].code,
                            searchInfo[i].comments,
                            searchInfo[i].likeds,
                            searchInfo[i].viewed,
                            searchInfo[i].createdAt,
                            searchInfo[i].header,
                            searchInfo[i].user_id,
                            searchInfo[i].is_deleted,
                            searchInfo[i].image))
                        if (searchInfo.size >0 ) last_id = searchInfo[i].id
                    }
                }
            }
            postArray.value = _postArray
        }
    }


}

//fun loadPostMore() {
//    viewModelScope.launch(CoroutineHandler().exceptionHandler) {
//        val postInfo:LoadPostDTO
//        if(App.prefs.myCode == App.prefs.key_all) {
//            postInfo = homeRepository.getAllPostMore("Bearer " + App.prefs.myJwt, last_id, 30)
//        } else {
//            postInfo = homeRepository.getPostMore(last_id,30,App.prefs.myCode)
//        }
//        for (i in 0 until postInfo.size) {
//            if(!postInfo[i].is_deleted) postArray.value?.add(postInfo[i])
//        }
//        if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
//        postArray.notifyObserver()
//    }
//}


//fun loadPost() {
//    viewModelScope.launch(CoroutineHandler().exceptionHandler) {
//        val postInfo = homeRepository.getPost(App.prefs.myCode)
//        for (i in 0 until postInfo.size) {
//            if(!postInfo[i].is_deleted) postArray.value?.add(postInfo[i]) // isDeleted가 아닌 게시물만 불러온다.
//        }
//        if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
//        postArray.notifyObserver()
//    }
//}
//private fun searchPosting() {
//    service?.getSearchPost()?.enqueue(object : Callback<LoadPostDTO?> {
//        override fun onResponse(
//            call: Call<LoadPostDTO?>?,
//            response: Response<LoadPostDTO?>
//
//        ) {
//            if (response.isSuccessful) {
//                showProgress(false)
//                val result: LoadPostDTO = response.body()!!
//                if (result.size != 0) {
//                    for (i in 0 until result.size) {
//                        if(result[i].title == target || result[i].content == target)
//                        {
//                            System.out.println(target)
//                            postingDTOlist?.add(
//                                LoadPostItem(
//                                    result[i].id,
//                                    result[i].title,
//                                    result[i].content,
//                                    result[i].author,
//                                    result[i].code,
//                                    result[i].comments,
//                                    result[i].likeds,
//                                    result[i].viewed,
//                                    result[i].createdAt,
//                                    result[i].header,
//                                    result[i].user_id,
//                                    result[i].is_deleted,
//                                    result[i].image
//                                )
//                            )
//                            System.out.println(postingDTOlist)
//                        }
//
//                        lastboard_id = result[i].id
//                    }
//
//                    // 어댑터 연결
//                    initAdapter()
//
//                } else {
//                    //TODO
//                }
//
//            } else {
//                // 실패시 resopnse.errorbody를 객체화
//                val gson = Gson()
//                val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                    LoadPostDTO::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        val result: LoadPostDTO = adapter.fromJson(
//                            response.errorBody()!!.string()
//                        )
//
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//            showProgress(false)
//            Log.e("onFailure", t.message!!)
//        }
//    })

//    private fun searchPostingMore(target: String, board_id: String) {
//        service?.getSearchPostMore("Bearer "+ App.prefs.myJwt,board_id,loadLimit)?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if (response.isSuccessful) {
//                    showProgress(false)
//                    val result: LoadPostDTO = response.body()!!
//                    if (result.size != 0) {
//                        for (i in 0 until result.size) {
//                            if(result[i].title == target || result[i].content == target)
//                            {
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
//                            searchAdapter.notifyDataSetChanged()
//                        }
//
//                        // 어댑터 연결
//                        initAdapter()
//
//                    } else {
//                        //TODO
//                    }
//
//                } else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                        LoadPostDTO::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            val result: LoadPostDTO = adapter.fromJson(
//                                response.errorBody()!!.string()
//                            )
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                showProgress(false)
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }