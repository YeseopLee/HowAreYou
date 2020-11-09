package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

data class SigninResponseDTO(

    @SerializedName("jwt") val jwt : String,
    @SerializedName("message") val message : List<Message>

)


