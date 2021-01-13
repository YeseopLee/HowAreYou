package com.example.howareyou.repository

import com.example.howareyou.model.*
import com.example.howareyou.network.ServiceApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val service: ServiceApi
) : DetailRepository {
    override suspend fun getPostContent(board_id: String): LoadPostItem {
        return service.getPostContent(board_id)
    }

    override suspend fun userComment(
        authHeader: String,
        data: PostCommentDTO
    ): PostCommentResponseDTO {
        return service.userComment(authHeader, data)
    }

    override suspend fun uploadFile(
        imageFile: ArrayList<MultipartBody.Part>,
        ref: RequestBody,
        refId: RequestBody,
        field: RequestBody
    ): UploadImageResponseDTO {
        return service.uploadFile(imageFile, ref, refId, field)
    }

    override suspend fun userLiked(data: PostLikedDTO): PostingResponseDTO {
        return service.userLiked(data)
    }

    override suspend fun deletePost(authHeader: String, board_id: String, data: deleteDTO) {
        service.deletePost(authHeader, board_id, data)
    }

    override suspend fun getAlarms(): AlarmResponseDTO {
        return service.getAlarms()
    }

    override suspend fun postAlarm(authHeader: String, data: AlarmDTO) {
        service.postAlarm(authHeader, data)
    }

    override suspend fun deleteAlarm(authHeader: String, alarm_id: String, board: String) {
        service.deleteAlarm(authHeader, alarm_id, board)
    }

}
