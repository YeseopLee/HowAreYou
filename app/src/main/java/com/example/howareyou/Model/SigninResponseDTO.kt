package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName

data class SigninResponseDTO(

    @SerializedName("jwt") val jwt : String,
    @SerializedName("user") val user: User,
    @SerializedName("message") val message : List<Message>

)

data class User(
    val blocked: Any,
    val email: String,
    val _id: String,
    val username: String
)

data class Message(
    val messages: List<MessageX>
)

data class MessageX(
    val id: String,
    val message: String
)


