package com.example.howareyou.model

import com.google.gson.annotations.SerializedName


data class ImageDTO(

    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("image") val image: String

)