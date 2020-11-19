package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

data class PostCommentDTO(
    @SerializedName("email") val email : String,
    @SerializedName("author") val author : String,
    @SerializedName("content") val content : String,
    @SerializedName("user_id") val user_id : String,
    @SerializedName("board") val board : String
)