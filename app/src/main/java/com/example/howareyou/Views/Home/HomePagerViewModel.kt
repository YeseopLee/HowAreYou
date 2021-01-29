package com.example.howareyou.views.home

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.*
import com.example.howareyou.repository.HomeRepository
import com.example.howareyou.util.CoroutineHandler
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class HomePagerViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    lateinit var last_id: String
    val isLoading = ObservableBoolean()
    val initLoading = ObservableBoolean()
    var postArray = MutableLiveData<ArrayList<LoadPostItem>>()

    init {
        initLoading.set(true)
        postArray.value = LoadPostDTO()
        boardBranch()
        Log.e("CodeTest",App.prefs.myCode)
    }


    fun boardBranch() {
        postArray.value?.clear()
        initLoading.set(true)
        when(App.prefs.myCode){
            App.prefs.key_all -> loadPostAll()
            else -> loadPost()
        }
    }

    fun loadPostAll() {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val postInfo = homeRepository.getAllPost("Bearer " + App.prefs.myJwt)
            for (i in 0 until postInfo.size) {
                if(!postInfo[i].is_deleted) {
                    postArray.value?.add(postInfo[i])
                } // isDeleted가 아닌 게시물만 불러온다.
            }
            postArray.notifyObserver()
            last_id = postInfo[postInfo.size - 1].id
            initLoading.set(false)
        }

    }

    fun loadPost() {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val postInfo = homeRepository.getPost(App.prefs.myCode)
            for (i in 0 until postInfo.size) {
                if(!postInfo[i].is_deleted) postArray.value?.add(postInfo[i]) // isDeleted가 아닌 게시물만 불러온다.
            }
            if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
            postArray.notifyObserver()
            initLoading.set(false)
        }
    }

    fun onRefresh() {
        isLoading.set(true)
        postArray.value = LoadPostDTO()
        when(App.prefs.myCode){
            App.prefs.key_all -> loadPostAll()
            else -> loadPost()
        }
        //postArray.notifyObserver()

        /**
         * Livedata.value.clear() 선언 후 notifyObserver() 실행해도 같은 동작
         **/

        isLoading.set(false)
    }

    fun loadPostAllMore() {

        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val postInfo = homeRepository.getAllPostMore("Bearer " + App.prefs.myJwt, last_id, 30)
            for (i in 0 until postInfo.size) {
                if(!postInfo[i].is_deleted) postArray.value?.add(postInfo[i])
            }
            if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
            postArray.notifyObserver()
        }
    }

    fun loadPostMore() {
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val postInfo:LoadPostDTO
            if(App.prefs.myCode == App.prefs.key_all) {
                postInfo = homeRepository.getAllPostMore("Bearer " + App.prefs.myJwt, last_id, 30)
            } else {
                postInfo = homeRepository.getPostMore(last_id,30,App.prefs.myCode)
            }
            for (i in 0 until postInfo.size) {
                if(!postInfo[i].is_deleted) postArray.value?.add(postInfo[i])
            }
            if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
            postArray.notifyObserver()
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }


}