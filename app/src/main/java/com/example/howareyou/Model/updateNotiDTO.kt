package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName


data class updateNotiDTO(

    @SerializedName("viewed") val viewed: Boolean

)