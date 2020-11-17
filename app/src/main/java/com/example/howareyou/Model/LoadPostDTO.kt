package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

class LoadPostDTO : ArrayList<LoadPostItem>()

data class LoadPostItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: String,
    @SerializedName("comments") val comments: List<Comment>?,
    @SerializedName("likeds") val likeds: List<Liked>?,
    @SerializedName("viewed") val viewed: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("header") val header: String,
    @SerializedName("user_id") val user_id: String, // 글쓴이 id = 사용자 id 수정 삭제 위해 불러옴
    @SerializedName("is_delected") val is_delected: Boolean
//    @SerializedName("image") val image: List<LImage>
)

data class Code(
    val __v: Int,
    val _id: String,
    val board: String,
    val code: String,
    val code_name: String,
    val code_type_name: String,
    val createdAt: String,
    val id: String,
    val published_at: String,
    val type_code: String,
    val updatedAt: String
)

data class Comment(
    val id: String,
    val author: String,
    val user_id: String,
    val board: String,
    val comment: String,
    val content: String,
    val createdAt: String
)

data class LImage(
    val __v: Int,
    val _id: String,
    val alternativeText: String,
    val caption: String,
    val createdAt: String,
    val ext: String,
    val formats: LFormats,
    val hash: String,
    val height: Int,
    val id: String,
    val mime: String,
    val name: String,
    val provider: String,
    val related: List<String>,
    val size: Double,
    val updatedAt: String,
    val url: String,
    val width: Int
)

data class LFormats(
    val small: LSmall,
    val thumbnail: LThumbnail
)

data class LThumbnail(
    val ext: String,
    val hash: String,
    val height: Int,
    val mime: String,
    val name: String,
    val path: Any,
    val size: Double,
    val url: String,
    val width: Int
)

data class LSmall(
    val ext: String,
    val hash: String,
    val height: Int,
    val mime: String,
    val name: String,
    val path: Any,
    val size: Double,
    val url: String,
    val width: Int
)

data class Liked(
    val board: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val user_id: String
)

data class Report(
    val __v: Int,
    val _id: String,
    val board: String,
    val createdAt: String,
    val id: String,
    val published_at: String,
    val updatedAt: String,
    val user_id: String
)

