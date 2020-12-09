package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

class NotiResponseDTO : ArrayList<NotiItem>()

data class NotiItem(
    @SerializedName("user_id") val user_id : String,
    @SerializedName("content") val content : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("board") val board : Board,
    @SerializedName("_id") val _id : String,
    @SerializedName("viewed") val viewed : Boolean
)

data class Board(
    val title: String,
    val _id: String
)