package com.example.howareyou.repository

import com.example.howareyou.model.LoadPostDTO
import com.example.howareyou.model.NotiResponseDTO
import com.example.howareyou.model.updateNotiDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchRepository {

    suspend fun getSearchPost() : LoadPostDTO

    suspend fun getSearchPostMore(authHeader: String, id_lt: String, _limit: Int) : LoadPostDTO

}

