package com.example.howareyou.model

import com.google.gson.annotations.SerializedName


data class updateNotiDTO(

    @SerializedName("viewed") val viewed: Boolean

)