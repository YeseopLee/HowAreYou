package com.example.howareyou.repository

import com.example.howareyou.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NotiRepository {

    suspend fun updateNoti(noti_id: String, data: updateNotiDTO)

    suspend fun getNoti() : NotiResponseDTO

    suspend fun deleteNoti(noti_id: String)

}
