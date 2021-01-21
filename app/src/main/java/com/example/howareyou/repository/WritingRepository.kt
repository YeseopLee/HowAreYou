package com.example.howareyou.repository

import com.example.howareyou.model.PostingDTO
import com.example.howareyou.model.PostingResponseDTO
import com.example.howareyou.model.UploadImageResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface WritingRepository {

    suspend fun userPost(authHeader: String, data: PostingDTO?) : PostingResponseDTO

    suspend fun uploadFile(imageFile: ArrayList<MultipartBody.Part>, ref: RequestBody, refId: RequestBody, field: RequestBody) : UploadImageResponseDTO

}

