package com.example.howareyou.repository

import com.example.howareyou.model.*

interface HomeRepository {

    suspend fun getAllPost(
        Auth: String
    ) : LoadPostDTO

    suspend fun getCode(

    ) : LoadCodeResponseDTO

    suspend fun getUserSet() : UpdateSetResponseDTO

    suspend fun updateUserSet(setting_id: String, data: PostdeviceTokenDTO)
}