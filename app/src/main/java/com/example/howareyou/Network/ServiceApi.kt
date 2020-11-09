package com.example.howareyou.network

import com.example.howareyou.Model.*
import com.example.howareyou.Model.SigninDTO
import com.example.howareyou.Model.SigninResponseDTO
import com.example.howareyou.Model.SignupDTO
import com.example.howareyou.Model.SignupResponseDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ServiceApi {

    @POST("/auth/local/register")
    fun userJoin(@Body data: SignupDTO?): Call<SignupResponseDTO?>?

    @POST("/auth/local")
    fun userLogin(@Body data: SigninDTO?): Call<SigninResponseDTO?>?

    @POST("/boards")
    fun userPost(@Body data: PostingDTO?): Call<PostingResponseDTO?>?

}