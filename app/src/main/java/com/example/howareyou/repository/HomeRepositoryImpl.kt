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

    override suspend fun getAllPostMore(Auth: String, id_lt: String, _limit: Int): LoadPostDTO {
        return service.getAllPostMore(Auth, id_lt, _limit)
    }

    override suspend fun getPost(code: String): LoadPostDTO {
        return service.getPost(code)
    }

    override suspend fun getPostMore(id_lt: String, _limit: Int, code: String): LoadPostDTO {
        return service.getPostMore(id_lt,_limit,code)
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