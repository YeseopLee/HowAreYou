package com.example.howareyou.util

import androidx.core.content.ContextCompat
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionCheck(val permissionActivity: Activity, val requirePermissions: Array<String>) {

    private val permissionRequestCode = 100

    // 권한 체크
    init {
        var failRequestPermissionList = ArrayList<String>()

        for (permission in requirePermissions) {
            if(ContextCompat.checkSelfPermission(permissionActivity.applicationContext,permission) != PackageManager.PERMISSION_GRANTED){
                failRequestPermissionList.add(permission)
            }
        }

        if (failRequestPermissionList.isNotEmpty()){
            val array = arrayOfNulls<String>(failRequestPermissionList.size)
            ActivityCompat.requestPermissions(permissionActivity,failRequestPermissionList.toArray(array),permissionRequestCode)
        }
    }

    fun permissionCheck(){
        var failRequestPermissionList = ArrayList<String>()

        for (permission in requirePermissions) {
            if(ContextCompat.checkSelfPermission(permissionActivity.applicationContext,permission) != PackageManager.PERMISSION_GRANTED){
                failRequestPermissionList.add(permission)
            }
        }

        if (failRequestPermissionList.isNotEmpty()){
            val array = arrayOfNulls<String>(failRequestPermissionList.size)
            ActivityCompat.requestPermissions(permissionActivity,failRequestPermissionList.toArray(array),permissionRequestCode)
        }
    }


//    fun readyCamera(context: Context){
//        val permission = ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA)
//        if (permission == PackageManager.PERMISSION_GRANTED){ // 카메라 권한이 있는경우
//            startCamera()
//        } else {
//            requestCameraPermission()
//        }
//    }
//
//    fun requestCameraPermission() {
//
//    }
}