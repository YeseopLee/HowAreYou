package com.example.howareyou

import com.google.gson.annotations.SerializedName


data class DummyPostingDTO(

    @SerializedName("id") val id: String?, // email은 private 설정을 하여서 null값으로 넘어올 수 있음.
    @SerializedName("author") val author: String,
    @SerializedName("header") val header: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("board_code") val board_code: Int,
    @SerializedName("__v") val __v: Int,
    @SerializedName("createdAt") val createdAt: Int,
    @SerializedName("updatedAt") val updatedAt: String

    )