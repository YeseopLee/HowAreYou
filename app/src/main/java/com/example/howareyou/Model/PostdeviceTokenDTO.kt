package com.example.howareyou.model

import com.google.gson.annotations.SerializedName

data class PostdeviceTokenDTO(

    @SerializedName("user_id") val user_id : String,
    @SerializedName("token") val token : String,
    @SerializedName("push_allowed") val push_allowed : Boolean

)