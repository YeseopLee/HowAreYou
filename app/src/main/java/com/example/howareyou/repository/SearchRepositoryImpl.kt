package com.example.howareyou.repository

import com.example.howareyou.model.LoadPostDTO
import com.example.howareyou.network.ServiceApi
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val service: ServiceApi
) : SearchRepository {
    override suspend fun getSearchPost(): LoadPostDTO {
        return service.getSearchPost()
    }

    override suspend fun getSearchPostMore(
        authHeader: String,
        id_lt: String,
        _limit: Int
    ): LoadPostDTO {
        return service.getSearchPostMore(authHeader, id_lt, _limit)
    }

}