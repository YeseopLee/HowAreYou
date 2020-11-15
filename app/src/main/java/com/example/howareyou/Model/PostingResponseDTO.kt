package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName


data class PostingResponseDTO(

    @SerializedName("id") val id : String,
    @SerializedName("message") val message : String

    )