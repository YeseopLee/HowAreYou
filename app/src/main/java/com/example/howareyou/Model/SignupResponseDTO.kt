package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class SignupResponseDTO(

    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String
)