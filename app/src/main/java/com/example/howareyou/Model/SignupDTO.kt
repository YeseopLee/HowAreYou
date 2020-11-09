package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

data class SignupDTO(

    @SerializedName("email") val email : String,
    @SerializedName("username") val username : String,
    @SerializedName("password") val password : String

)

