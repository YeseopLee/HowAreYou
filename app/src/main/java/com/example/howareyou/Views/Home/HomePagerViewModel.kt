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

        loadPost()
    }

    fun loadPost() {

        viewModelScope.launch {
            val postInfo = homeRepository.getAllPost("Bearer " + App.prefs.myJwt)
            postArray.value = postInfo
            last_id = postInfo[postInfo.size - 1].id
        }
    }

    fun onRefresh() {
        isLoading.set(true)
        postArray.value?.clear()
        loadPost()
        postArray.notifyObserver()
        Log.e("HomeViewModel",postArray.value.toString())
        isLoading.set(false)
    }

    fun loadPostMore() {

        viewModelScope.launch {
            val postInfo = homeRepository.getAllPostMore("Bearer " + App.prefs.myJwt, last_id, 30)
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