package com.example.howareyou.repository

import com.example.howareyou.model.*
import com.example.howareyou.network.ServiceApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class NotiRepositoryImpl @Inject constructor(
    private val service: ServiceApi
) : NotiRepository {

    override suspend fun updateNoti(noti_id: String, data: updateNotiDTO) {
        service.updateNoti(noti_id,data)
    }

    override suspend fun getNoti(): NotiResponseDTO {
        return service.getNoti()
    }

    override suspend fun deleteNoti(noti_id: String) {
        service.deleteNoti(noti_id)
    }


}


