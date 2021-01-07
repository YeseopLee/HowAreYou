package com.example.howareyou.model


class AlarmResponseDTO : ArrayList<alarmItem>()

data class alarmItem(
    val _id: String,
    val board: Board,
    val user_id: String
)