package com.example.howareyou.repository

import com.example.howareyou.model.LoadCodeResponseDTO
import com.example.howareyou.model.LoadPostDTO
import com.example.howareyou.model.PostdeviceTokenDTO
import com.example.howareyou.model.UpdateSetResponseDTO
import com.example.howareyou.network.ServiceApi
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val service: ServiceApi
) : HomeRepository {
    override suspend fun getAllPost(Auth: String): LoadPostDTO {
        return service.getAllPost(Auth)
    }

    override suspend fun getCode(): LoadCodeResponseDTO {
        return service.getCode()
    }

    override suspend fun getUserSet(): UpdateSetResponseDTO {
        return service.getUsersettings()
    }

    override suspend fun updateUserSet(setting_id: String, data: PostdeviceTokenDTO) {
        service.userUpdatesetting(setting_id, data)
    }

}