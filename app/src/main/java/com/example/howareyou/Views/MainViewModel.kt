package com.example.howareyou.views

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howareyou.App
import com.example.howareyou.model.LoadCodeResponseDTO
import com.example.howareyou.model.PostdeviceTokenDTO
import com.example.howareyou.model.StatuscodeResponse
import com.example.howareyou.model.UpdateSetResponseDTO
import com.example.howareyou.repository.HomeRepository
import com.example.howareyou.util.CoroutineHandler
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    init {
        getCode()
        getUserSet(App.prefs.myId)
    }


    fun getCode(){ //현재 게시판 코드 불러오기
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val codeInfo = homeRepository.getCode()
            for (i in 0 until codeInfo.size) {
                var temp = codeInfo[i].id
                when(codeInfo[i].id){
                    "01" -> App.prefs.codeFree = temp
                    "02" -> App.prefs.codeQA = temp
                    "03" -> App.prefs.codeTips = temp
                    "04" -> App.prefs.codeCourse = temp
                    "05" -> App.prefs.codeStudy = temp
                    "06" -> App.prefs.codeBest = temp
                }
            }
        }
    }

    fun getUserSet(user_id: String){ // 로그인한 사용자 id 기반 탐색
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            val userSetInfo = homeRepository.getUserSet()
            for (i in 0 until userSetInfo.size){
                if(userSetInfo[i].user_id == user_id){
                    updateDeviceToken(userSetInfo[i]._id)
                }
            }
        }
    }


    fun updateDeviceToken(setting_id : String){
        viewModelScope.launch(CoroutineHandler().exceptionHandler) {
            homeRepository.updateUserSet(setting_id, PostdeviceTokenDTO(App.prefs.myId, App.prefs.myDevice, true))
        }
    }

}

//
//// id를 기반으로 usersetting id값 검색
//private fun findSettingid(user_id : String) {
//    var tempSettingid : String = ""
//
//    service?.getUsersettings()?.enqueue(object :
//        Callback<UpdateSetResponseDTO?> {
//        override fun onResponse(
//            call: Call<UpdateSetResponseDTO?>?,
//            response: Response<UpdateSetResponseDTO?>
//        ) {
//            if(response.isSuccessful) {
//                val result = response.body()!!
//                for ( i in 0 until result.size){
//                    if(result[i].user_id == user_id)
//                    {
//                        tempSettingid = result[i]._id
//                        updateDeviceToken(tempSettingid) // setting id를 기반으로 devicetoken을 update 한다.
//                    }
//                }
//            }
//            else if (response.code() == 400) {
//                val gson = Gson()
//                val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
//                    StatuscodeResponse::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        val result : StatuscodeResponse = adapter.fromJson(response.errorBody()!!.string())
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<UpdateSetResponseDTO?>, t: Throwable) {
//            //
//        }
//
//    })
//}
//
////usersetting id값을 기반으로 device token update
//private fun updateDeviceToken(setting_id : String) {
//
//    service?.userUpdatesetting(setting_id,
//        PostdeviceTokenDTO(App.prefs.myId, App.prefs.myDevice,true)
//    )?.enqueue(object :
//        Callback<Void?> {
//        override fun onResponse(
//            call: Call<Void?>?,
//            response: Response<Void?>
//        ) {
//            if(response.isSuccessful) {
//
//            }
//            else if (response.code() == 400) {
//                val gson = Gson()
//                val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
//                    StatuscodeResponse::class.java
//                )
//                try {
//                    if (response.errorBody() != null) {
//                        val result : StatuscodeResponse = adapter.fromJson(response.errorBody()!!.string())
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<Void?>, t: Throwable) {
//            //
//        }
//
//    })
//}