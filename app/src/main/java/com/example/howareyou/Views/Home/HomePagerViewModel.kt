package com.example.howareyou.views.home

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.howareyou.App
import com.example.howareyou.Util.Event
import com.example.howareyou.model.*
import com.example.howareyou.repository.HomeRepository
import kotlinx.coroutines.launch


class HomePagerViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    lateinit var last_id: String
    val isLoading = ObservableBoolean()
    var postArray = MutableLiveData<ArrayList<LoadPostItem>>()

    init {
        Log.e("codeTest0",App.prefs.myCode)
        boardBranch()
        //loadPostAll()
    }

    fun boardBranch() {
        Log.e("codeTest",App.prefs.myCode)
        postArray.value?.clear()
        when(App.prefs.myCode){
            App.prefs.key_all -> loadPostAll()
            else -> loadPost()
        }
    }

    fun loadPostAll() {
        Log.e("codeTest0",App.prefs.myCode)
        viewModelScope.launch {
            val postInfo = homeRepository.getAllPost("Bearer " + App.prefs.myJwt)
            postArray.value = postInfo
            last_id = postInfo[postInfo.size - 1].id
        }
    }

    fun loadPost() {
        viewModelScope.launch {
            val postInfo = homeRepository.getPost(App.prefs.myCode)
            postArray.value = postInfo
            if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
        }
    }

    fun onRefresh() {
        isLoading.set(true)
        postArray.value?.clear()
        loadPostAll()
        postArray.notifyObserver()
        Log.e("HomeViewModel",postArray.value.toString())
        isLoading.set(false)
    }

    fun loadPostAllMore() {

        viewModelScope.launch {
            val postInfo = homeRepository.getAllPostMore("Bearer " + App.prefs.myJwt, last_id, 30)
            for (i in 0 until postInfo.size) {
                postArray.value?.add(postInfo[i])
            }
            if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
            postArray.notifyObserver()
        }
    }

    fun loadPostMore() {
        viewModelScope.launch {
            val postInfo:LoadPostDTO
            if(App.prefs.myCode == App.prefs.key_all) {
                postInfo = homeRepository.getAllPostMore("Bearer " + App.prefs.myJwt, last_id, 30)
            } else {
                postInfo = homeRepository.getPostMore(last_id,30,App.prefs.myCode)
            }
            for (i in 0 until postInfo.size) {
                postArray.value?.add(postInfo[i])
            }
            if (postInfo.size > 0) last_id = postInfo[postInfo.size - 1].id
            postArray.notifyObserver()
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }


}