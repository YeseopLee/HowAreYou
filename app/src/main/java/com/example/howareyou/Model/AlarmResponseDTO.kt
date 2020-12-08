package com.example.howareyou.Model

import com.google.gson.annotations.SerializedName


class AlarmResponseDTO : ArrayList<alarmItem>()

data class alarmItem(
    val _id: String,
    val board: Board,
    val user_id: String
)