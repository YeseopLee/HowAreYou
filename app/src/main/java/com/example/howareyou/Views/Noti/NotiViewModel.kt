package com.example.howareyou.views.noti

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.model.NotiItem
import com.example.howareyou.repository.NotiRepository
import kotlinx.coroutines.launch


class NotiViewModel @ViewModelInject constructor(
    private val notiRepository: NotiRepository
) : ViewModel() {

    var _notiArray = ArrayList<NotiItem>()
    var notiArray = MutableLiveData<ArrayList<NotiItem>>()

    init {
        loadNotification()
    }


    private fun loadNotification() {
        viewModelScope.launch {
            val notiInfo = notiRepository.getNoti()
            Log.e("Notitest0",notiInfo.toString())
            for ( i in 0 until notiInfo.size ){
                if(App.prefs.myId == notiInfo[i].user_id) _notiArray.add(NotiItem(notiInfo[i].user_id,notiInfo[i].content,notiInfo[i].createdAt,notiInfo[i].board,notiInfo[i]._id,notiInfo[i].viewed))
            }
            notiArray.value = _notiArray

            Log.e("NotiTEst",notiArray.value.toString())
        }
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }


}


//
//
//private fun loadNotification() {
//    service?.getNoti()?.enqueue(object : Callback<NotiResponseDTO?> {
//        override fun onResponse(
//            call: Call<NotiResponseDTO?>?,
//            response: Response<NotiResponseDTO?>
//
//        ) {
//            if (response.isSuccessful) {
//                showProgress(false)
//                val result: NotiResponseDTO = response.body()!!
//                for ( i in 0 until result.size){
//                    if(App.prefs.myId == result[i].user_id) notiDTOList.add(NotiItem(result[i].user_id,result[i].content,result[i].createdAt,result[i].board,result[i]._id,result[i].viewed))
//                }
//                Log.d("NotificationList",notiDTOList.toString())
//                notiAdapter.notifyDataSetChanged()
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
//        override fun onFailure(call: Call<NotiResponseDTO?>?, t: Throwable) {
//            Log.e("onFailure", t.message!!)
//            showProgress(false)
//        }
//    })
//
//}
