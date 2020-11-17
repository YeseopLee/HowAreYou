package com.example.howareyou.network

import com.example.howareyou.Model.*
import retrofit2.Call
import retrofit2.http.*


interface ServiceApi {

    @POST("/auth/local/register")
    fun userJoin(@Body data: SignupDTO?): Call<SignupResponseDTO?>?

    @POST("/auth/local")
    fun userLogin(@Body data: SigninDTO?): Call<SigninResponseDTO?>?

    @POST("/boards")
    fun userPost(@Header("authorization") authHeader :String, @Body data: PostingDTO?): Call<PostingResponseDTO?>?

    @GET("/Codes")
    fun getCode(): Call<LoadCodeResponseDTO>?

    @GET("/boards?_sort=id:DESC&_limit=10")
    fun getPost(@Query("code") code: String): Call<LoadPostDTO?>?

    @GET("/boards/{board_id}")
    fun getPostContent(@Path("board_id")board_id : String): Call<LoadPostItem>?

    /* dynamic query 예시 */
//    @GET("/api/users")
//    fun getUsers(
//        @Query("per_page") pageSize: Int,
//        @Query("page") currentPage: Int
//    ): Call<DTO?>?

//   /boards?_sort=id:DESC&_limit=10&code=5faf9473ec65907850b3f7b5&id_lt=5fb0ba33a9e87d59a4da5f29
}