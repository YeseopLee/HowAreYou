package com.example.howareyou.repository

import com.example.howareyou.model.*

interface HomeRepository {

    suspend fun getAllPost(
        Auth: String
    ) : LoadPostDTO
}