package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName


data class SigninDTO(

    @SerializedName("userID") val userEmail: String,
    @SerializedName("userPwd") val userPwd: String

)