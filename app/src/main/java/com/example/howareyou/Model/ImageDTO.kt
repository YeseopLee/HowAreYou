package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName


data class ImageDTO(

    @SerializedName("imageurl") val imageurl: String,
    @SerializedName("content") val content: String

)