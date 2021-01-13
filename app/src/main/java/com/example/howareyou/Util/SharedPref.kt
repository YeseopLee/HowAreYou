package com.example.howareyou.util

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context){

    val prefs_filename = "prefs"
    val key_myEmail = "email"
    val key_jwt = "jwt"
    val key_myName = "name"
    val key_myid = "id"

    val key_myDeviceToken = "device"

    val key_code = "code"
    val key_all = "all"
    val key_free = "free"
    val key_qa = "qa"
    val key_tip = "tip"
    val key_course = "course"
    val key_study = "study"
    val key_best = "best"

    val key_tempCommentid = "none"
    val key_pushed = "none"
    val key_notification_count = "notification"

    val prefs: SharedPreferences = context.getSharedPreferences(prefs_filename, 0)

    var myEmail: String
        get() = prefs.getString(key_myEmail, "")!!
        set(value) = prefs.edit().putString(key_myEmail, value).apply()

    var myName: String
        get() = prefs.getString(key_myName, "")!!
        set(value) = prefs.edit().putString(key_myName, value).apply()

    var myJwt: String
        get() = prefs.getString(key_jwt, "")!!
        set(value) = prefs.edit().putString(key_jwt, value).apply()

    var myId: String
        get() = prefs.getString(key_myid, "")!!
        set(value) = prefs.edit().putString(key_myid, value).apply()

    var myDevice: String
        get() = prefs.getString(key_myDeviceToken, "")!!
        set(value) = prefs.edit().putString(key_myDeviceToken, value).apply()

    var myCode : String
        get() = prefs.getString(key_code, "")!!
        set(value) = prefs.edit().putString(key_code, value).apply()

    var codeFree: String
        get() = prefs.getString(key_free, "")!!
        set(value) = prefs.edit().putString(key_free, value).apply()

    var codeQA: String
        get() = prefs.getString(key_qa, "")!!
        set(value) = prefs.edit().putString(key_qa, value).apply()

    var codeTips: String
        get() = prefs.getString(key_tip, "")!!
        set(value) = prefs.edit().putString(key_tip, value).apply()

    var codeCourse: String
        get() = prefs.getString(key_course, "")!!
        set(value) = prefs.edit().putString(key_course, value).apply()

    var codeStudy: String
        get() = prefs.getString(key_study, "")!!
        set(value) = prefs.edit().putString(key_study, value).apply()

    var codeBest: String
        get() = prefs.getString(key_best, "")!!
        set(value) = prefs.edit().putString(key_best, value).apply()

    var tempCommentId: String
        get() = prefs.getString(key_tempCommentid, "none")!!
        set(value) = prefs.edit().putString(key_tempCommentid, value).apply()

    var pushedId: String
        get() = prefs.getString(key_pushed, "none")!!
        set(value) = prefs.edit().putString(key_pushed, value).apply()

    var notificationCount: Int
        get() = prefs.getInt(key_notification_count, 0)!!
        set(value) = prefs.edit().putInt(key_notification_count, value).apply()

    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장 */
}