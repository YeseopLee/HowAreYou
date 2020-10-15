package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class SignupDTO(

    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String

)