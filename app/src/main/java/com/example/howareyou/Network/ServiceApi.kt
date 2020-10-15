package com.example.gagaotalk.network

import com.example.gagaotalk.Model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ServiceApi {

    @POST("/auth/join")
    fun userJoin(@Body data: SignupDTO?): Call<SignupResponseDTO?>?

    @POST("/auth/login")
    fun userLogin(@Body data: SigninDTO?): Call<SigninResponseDTO?>?

}