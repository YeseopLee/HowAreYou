package com.example.howareyou.repository

import com.example.howareyou.Model.*
import com.example.howareyou.network.ServiceApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (
    private val service: ServiceApi
) : AuthRepository {

    override suspend fun login(data: SigninDTO): SigninResponseDTO {
        return service.userLogin(data)
    }

    override suspend fun signup(data: SignupDTO): SignupResponseDTO {
        return service.userJoin(data)
    }

    override suspend fun postDeviceToken(data: PostdeviceTokenDTO) {
        service.userSetting(data)
    }
}