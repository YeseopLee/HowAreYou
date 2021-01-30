package com.example.howareyou.repository

import com.example.howareyou.model.*

interface AuthRepository {

    suspend fun login(
        data: SigninDTO
    ) : SigninResponseDTO

    suspend fun signup(
        data: SignupDTO
    ) : SignupResponseDTO

    suspend fun findPw(
        data: FindPasswordDTO
    )

    suspend fun postDeviceToken(
        data: PostdeviceTokenDTO
    )
}