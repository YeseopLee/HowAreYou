package com.example.howareyou.model

import com.google.gson.annotations.SerializedName

data class SignupResponseDTO(

//    @SerializedName("statusCode") val statusCode : Int,
//    @SerializedName("message") val message : List<Message>

    @SerializedName("user") val user : Userid
)

data class Userid(
    val _id: String
)

//data class Message(
//    val messages: List<MessageX>
//)
//
//data class MessageX(
//    val id: String,
//    val message: String
//)