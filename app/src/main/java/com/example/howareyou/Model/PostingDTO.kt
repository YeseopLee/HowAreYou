package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

data class PostingDTO(
    @SerializedName("email") val email: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("header") val header: String,
    @SerializedName("code") val code: String
    //@SerializedName("image") val image: List<Image>
)

//data class Code(
//    val __v: Int,
//    val _id: String,
//    val board: String,
//    val code: String,
//    val code_name: String,
//    val code_type_name: String,
//    val createdAt: String,
//    val id: String,
//    val published_at: String,
//    val type_code: String,
//    val updatedAt: String
//)

data class Image(
    val __v: Int,
    val _id: String,
    val alternativeText: String,
    val caption: String,
    val createdAt: String,
    val ext: String,
    val formats: Formats,
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

data class Formats(
    val small: Small,
    val thumbnail: Thumbnail
)

data class Thumbnail(
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

data class Small(
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