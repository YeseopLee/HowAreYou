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

    @POST("/comments")
    fun userComment(@Header("authorization") authHeader: String, @Body data: PostCommentDTO): Call<PostCommentDTO>?

    @POST("/likeds")
    fun userLiked(@Body data: PostLikedDTO): Call<PostingResponseDTO>?

    @GET("/Codes")
    fun getCode(): Call<LoadCodeResponseDTO>?

//    @GET("/boards")
//    fun getAllPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=_id:DESC&_limit=10")
    fun getAllPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=_id:DESC&_limit=10")
    fun getPost(@Query("code") code: String): Call<LoadPostDTO?>?

    @GET("/boards/{board_id}")
    fun getPostContent(@Path("board_id")board_id : String): Call<LoadPostItem>?

    @DELETE("boards/{board_id}")
    fun deletePost(@Header("authorization") authHeader: String, @Path("board_id")board_id: String): Call<Void>?

    /* dynamic query 예시 */
//    @GET("/api/users")
//    fun getUsers(
//        @Query("per_page") pageSize: Int,
//        @Query("page") currentPage: Int
//    ): Call<DTO?>?

//   /boards?_sort=id:DESC&_limit=10&code=5faf9473ec65907850b3f7b5&id_lt=5fb0ba33a9e87d59a4da5f29
}