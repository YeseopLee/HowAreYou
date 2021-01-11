package com.example.howareyou.repository

import com.example.howareyou.model.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HomeRepository {

    suspend fun getAllPost(
        Auth: String
    ): LoadPostDTO

    suspend fun getAllPostMore(
        Auth: String,
        id_lt: String,
        _limit: Int
    ): LoadPostDTO

    suspend fun getPost(
        code: String
    ) : LoadPostDTO

    suspend fun getPostMore(
        id_lt: String,
        _limit: Int,
        code: String
    ) : LoadPostDTO

    suspend fun getCode(

    ): LoadCodeResponseDTO

    suspend fun getUserSet(): UpdateSetResponseDTO

    suspend fun updateUserSet(setting_id: String, data: PostdeviceTokenDTO)



}


