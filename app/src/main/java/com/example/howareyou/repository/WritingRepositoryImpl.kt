package com.example.howareyou.repository

import com.example.howareyou.model.LoadPostDTO
import com.example.howareyou.model.PostingDTO
import com.example.howareyou.model.PostingResponseDTO
import com.example.howareyou.model.UploadImageResponseDTO
import com.example.howareyou.network.ServiceApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class WritingRepositoryImpl @Inject constructor(
    private val service: ServiceApi
) : WritingRepository {
    override suspend fun userPost(authHeader: String, data: PostingDTO?): PostingResponseDTO {
        return service.userPost(authHeader, data)
    }

    override suspend fun uploadFile(
        imageFile: ArrayList<MultipartBody.Part>,
        ref: RequestBody,
        refId: RequestBody,
        field: RequestBody
    ): UploadImageResponseDTO {
        return service.uploadFile(imageFile, ref, refId, field)
    }
}