package com.example.howareyou.repository

import com.example.howareyou.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface DetailRepository {

    suspend fun getPostContent(board_id: String) : LoadPostItem

    suspend fun userComment(authHeader: String, data: PostCommentDTO) : PostCommentResponseDTO

    suspend fun uploadFile(imageFile: ArrayList<MultipartBody.Part>, ref: RequestBody, refId: RequestBody, field: RequestBody) : UploadImageResponseDTO

    suspend fun userLiked(data: PostLikedDTO) : PostingResponseDTO

    suspend fun deletePost(authHeader: String, board_id: String, data: deleteDTO)

    suspend fun getAlarms() : AlarmResponseDTO

    suspend fun postAlarm(authHeader: String, data: AlarmDTO)

    suspend fun deleteAlarm(authHeader: String, alarm_id: String, board: String)

}

