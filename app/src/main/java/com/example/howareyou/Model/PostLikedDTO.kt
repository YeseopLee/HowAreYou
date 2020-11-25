package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

data class PostLikedDTO(

    @SerializedName("email") val identifier: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("board") val board: String?,
    @SerializedName("comment") val comment: String?

)