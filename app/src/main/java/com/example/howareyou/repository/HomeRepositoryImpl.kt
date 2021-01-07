package com.example.howareyou.repository

import com.example.howareyou.model.LoadPostDTO
import com.example.howareyou.network.ServiceApi
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val service: ServiceApi
) : HomeRepository {
    override suspend fun getAllPost(Auth: String): LoadPostDTO {
        return service.getAllPost(Auth)
    }

}