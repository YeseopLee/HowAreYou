package com.example.howareyou.Model

data class User(
    val blocked: Any,
    val boards: List<Any>,
    val comments: List<Any>,
    val confirmed: Boolean,
    val created_at: String,
    val email: String,
    val id: Int,
    val provider: String,
    val role: Role,
    val updated_at: String,
    val username: String
)