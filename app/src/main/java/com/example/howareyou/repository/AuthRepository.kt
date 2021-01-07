package com.example.howareyou.repository

import com.example.howareyou.Model.*

interface AuthRepository {

    suspend fun login(
        data: SigninDTO
    ) : SigninResponseDTO

    suspend fun signup(
        data: SignupDTO
    ) : SignupResponseDTO

    suspend fun postDeviceToken(
        data: PostdeviceTokenDTO
    )
}