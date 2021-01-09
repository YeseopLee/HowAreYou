package com.example.howareyou.network

import com.example.howareyou.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ServiceApi {

    companion object {
        const val BASE_URL = "http://211.208.220.233:1337"
    }

    @POST("/auth/local/register")
    suspend fun userJoin(@Body data: SignupDTO?): SignupResponseDTO

    @POST("/auth/local")
    suspend fun userLogin(@Body data: SigninDTO?): SigninResponseDTO

    @POST("/boards")
    fun userPost(@Header("authorization") authHeader :String, @Body data: PostingDTO?): Call<PostingResponseDTO?>?

    @POST("/comments")
    fun userComment(@Header("authorization") authHeader: String, @Body data: PostCommentDTO): Call<PostCommentResponseDTO>?

    @POST("/likeds")
    fun userLiked(@Body data: PostLikedDTO): Call<PostingResponseDTO>?

    @POST("/usersettings")
    suspend fun userSetting(@Body data: PostdeviceTokenDTO): Response<Unit>

    @POST("/alarms")
    fun postAlarm(@Header("authorization") authHeader: String, @Body data: AlarmDTO): Call<Void>?

//    @Multipart
//    @POST("/upload")
//    fun uploadFile(@Part imageFile: MultipartBody.Part): Call<UploadImageResponseDTO>?

    @Multipart
    @POST("/upload")
    fun uploadFile(@Part imageFile: ArrayList<MultipartBody.Part>, @Part ("ref") ref: RequestBody, @Part ("refId") refId: RequestBody, @Part ("field") field: RequestBody): Call<UploadImageResponseDTO>?

    @PUT("/usersettings/{id}")
    suspend fun userUpdatesetting(@Path("id")setting_id : String, @Body data: PostdeviceTokenDTO): Response<Unit>

    @PUT("/notifications/{noti_id}")
    fun updateNoti(@Path("noti_id")noti_id: String, @Body data: updateNotiDTO): Call<Void>?

    @PUT("boards/{board_id}")
    fun deletePost(@Header("authorization") authHeader: String, @Path("board_id")board_id: String, @Body data: deleteDTO ): Call<Void>?

    @GET("/Codes")
    suspend fun getCode(): LoadCodeResponseDTO

//    @GET("/boards")
//    fun getAllPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=_id:DESC&_limit=30")
    suspend fun getAllPost(@Header("authorization") authHeader: String): LoadPostDTO

    @GET("/boards?_sort=_id:DESC")
    suspend fun getAllPostMore(@Header("authorization") authHeader: String, @Query ("id_lt")id_lt: String, @Query("_limit")_limit : Int): LoadPostDTO

    @GET("/boards?_sort=_id:DESC&_limit=100")
    fun getSearchPost(): Call<LoadPostDTO?>?

    @GET("/boards?_sort=_id:DESC")
    fun getSearchPostMore(@Header("authorization") authHeader: String, @Query ("id_lt")id_lt: String, @Query("_limit")_limit : Int): Call<LoadPostDTO?>?

    @GET("/boards?_sort=_id:DESC&_limit=30")
    fun getPost(@Query("code") code: String): Call<LoadPostDTO?>?

    @GET("/boards?_sort=_id:DESC")
    fun getPostMore( @Query ("id_lt")id_lt: String, @Query("_limit")_limit : Int, @Query("code") code: String): Call<LoadPostDTO?>?

    @GET("/boards/{board_id}")
    fun getPostContent(@Path("board_id")board_id : String): Call<LoadPostItem>?

    @GET("/{url}")
    fun getImage(@Path("url")url : String): Call<imageItem>?

    @GET("/notifications")
    fun getNoti(): Call<NotiResponseDTO>?

    @GET("/usersettings")
    suspend fun getUsersettings(): UpdateSetResponseDTO

    @GET("/alarms")
    fun getAlarms(): Call<AlarmResponseDTO>?

    @DELETE("/notifications/{noti_id}")
    fun deleteNoti(@Path("noti_id")noti_id: String): Call<Void>?

    @DELETE("/alarms/{alarm_id}")
    fun deleteAlarm(@Header("authorization") authHeader: String, @Path("alarm_id")alarm_id : String, @Query("board")board: String): Call<Void>?

    /* dynamic query 예시 */
//    @GET("/api/users")
//    fun getUsers(
//        @Query("per_page") pageSize: Int,
//        @Query("page") currentPage: Int
//    ): Call<DTO?>?

//   /boards?_sort=id:DESC&_limit=10&code=5faf9473ec65907850b3f7b5&id_lt=5fb0ba33a9e87d59a4da5f29
}