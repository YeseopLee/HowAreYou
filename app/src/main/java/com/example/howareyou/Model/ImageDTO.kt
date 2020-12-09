package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName


data class ImageDTO(

    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("image") val image: String

)