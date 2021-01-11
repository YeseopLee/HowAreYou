package com.example.howareyou.Util

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class ConvertTime  //GMT to KOREA
{
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun convertTimeZone(time: String): String {
        val form = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outform = "yyyy-MM-dd'T'HH:mm:ss"
        val inputFormat = SimpleDateFormat(form, Locale.KOREA)
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val outputFormat = SimpleDateFormat(outform)
        // Adjust locale and zone appropriately
        val date: Date = inputFormat.parse(time)
        return outputFormat.format(date)
    }

    fun currentTime() : String {
        val mNow = System.currentTimeMillis()
        val mReDate = Date(mNow)
        val mFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        return mFormat.format(mReDate)
    }

    // 하루 이내 / 한시간 이내의 글 별도 표기 위함
    @SuppressLint("SimpleDateFormat")
    fun showTime(time: String) : String {

        val postTime = convertTimeZone(time)
        val currentTime = currentTime()

        val today = "MM-dd"
        val notToday = "yyyy-MM-dd"
        val date: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(postTime)

        // 오늘 글인지 확인하기
        if (postTime.slice(0..9) == currentTime.slice(0..9)){
            val diff : Int = (currentTime.slice(11..12).toInt()*60 + currentTime.slice(14..15).toInt()) - (postTime.slice(11..12).toInt()*60 + postTime.slice(14..15).toInt())
            return if( diff < 60) diff.toString()+"분 전"
            else SimpleDateFormat(today).format(date)
        }
        return SimpleDateFormat(notToday).format(date)
    }

}
