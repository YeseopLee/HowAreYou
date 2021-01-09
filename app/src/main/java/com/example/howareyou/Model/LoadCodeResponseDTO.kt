package com.example.howareyou.model

class LoadCodeResponseDTO : ArrayList<CodeItem>()

data class CodeItem(
    val __v: Int,
    val _id: String,
    val board: String,
    val code: String,
    val code_name: String,
    val code_type_name: String,
    val createdAt: String,
    val id: String,
    val published_at: String,
    val type_code: String,
    val updatedAt: String
)
