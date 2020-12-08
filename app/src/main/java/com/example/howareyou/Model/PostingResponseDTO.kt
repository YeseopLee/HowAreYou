package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName


data class PostingResponseDTO(

    @SerializedName("_id") val _id : String,
    @SerializedName("message") val message : String

    )