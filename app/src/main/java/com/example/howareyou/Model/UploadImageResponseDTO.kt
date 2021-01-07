package com.example.howareyou.model

class UploadImageResponseDTO: ArrayList<imageItem>()

data class imageItem(
    val _id: String,
    val url: String
)