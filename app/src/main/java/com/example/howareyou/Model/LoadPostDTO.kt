package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

class LoadPostDTO : ArrayList<PostItem>()

data class PostItem(
    @SerializedName("email") val email: String,
    @SerializedName("board_category") val board_category: String,
    @SerializedName("header") val header: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("liked") val liked: Int,
    @SerializedName("views") val views: Int,
    @SerializedName("reported") val reported: Int,
    @SerializedName("is_deleted") val is_deleted: Boolean,
    @SerializedName("comments_no") val comments_no: Int,
    @SerializedName("created_at") val created_at: String
)

