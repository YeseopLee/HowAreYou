package com.example.howareyou.model

import com.google.gson.annotations.SerializedName


data class PostingResponseDTO(

    @SerializedName("_id") val _id : String,
    @SerializedName("message") val message : String

    )