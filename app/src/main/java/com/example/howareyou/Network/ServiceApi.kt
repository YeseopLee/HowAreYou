package com.example.howareyou.network

import com.example.howareyou.Model.*
import com.example.howareyou.Model.SigninDTO
import com.example.howareyou.Model.SigninResponseDTO
import com.example.howareyou.Model.SignupDTO
import com.example.howareyou.Model.SignupResponseDTO
import retrofit2.Call
import retrofit2.http.*

/*
    board_category branch
    01 = freeboard
    02 = QAboard
    03 = Tipsboard
    04 = Courseboard
    05 = Studyboard
    06 = Bestboard
*/


interface ServiceApi {

    @POST("/auth/local/register")
    fun userJoin(@Body data: SignupDTO?): Call<SignupResponseDTO?>?

    @POST("/auth/local")
    fun userLogin(@Body data: SigninDTO?): Call<SigninResponseDTO?>?

    @POST("/boards")
    fun userPost(@Header("authorization") authHeader :String, @Body data: PostingDTO?): Call<PostingResponseDTO?>?

    @GET("/boards?_sort=id:DESC&id_lt=50&_limit=10&board_category=01")
    fun getFreePost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=id:DESC&id_lt=50&_limit=10&board_category=02")
    fun getQAPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=id:DESC&id_lt=50&_limit=10&board_category=03")
    fun getTipsPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=id:DESC&id_lt=50&_limit=10&board_category=04")
    fun getCoursePost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=id:DESC&id_lt=50&_limit=10&board_category=05")
    fun getStudyPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=id:DESC&id_lt=50&_limit=10&board_category=06")
    fun getBestPost(): Call<LoadPostDTO?>?

    // user id 쿼리 필요

}