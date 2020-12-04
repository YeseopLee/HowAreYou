package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

class UploadImageResponseDTO: ArrayList<imageItem>()

data class imageItem(
    val _id: String,
    val url: String
)