package com.example.howareyou.Model

class UpdateSetResponseDTO : ArrayList<UpdateItem>()

data class UpdateItem(
    val _id: String,
    val user_id: String
)