package com.example.howareyou.Util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ConvertTime  //GMT to KOREA
{
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun convertTimeZone(time: String?): String {
        val form = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val inputFormat = SimpleDateFormat(form, Locale.KOREA)
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val outputFormat = SimpleDateFormat(form)
        // Adjust locale and zone appropriately
        val date: Date = inputFormat.parse(time)
        return outputFormat.format(date)
    }

    fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }

    fun convtest(date: String): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val result = formatter.parse(date)
        return result.toString()
    }

}
