package com.example.gagaotalk.Model

import com.google.gson.annotations.SerializedName

data class SignupResponseDTO(

    @SerializedName("statusCode") val statusCode : Int,
    @SerializedName("message") val message : List<Message>
)

data class Message(
    val messages: List<MessageX>
)

data class MessageX(
    val id: String,
    val message: String
)