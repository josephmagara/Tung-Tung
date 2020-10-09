package com.example.tungtung.data.utils

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject


/**
 * Created by josephmagara on 9/10/20.
 */
class PermissionsHelper @Inject constructor(private val context: Context) {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
    }
    private val requiredCameraPermissions = arrayOf(Manifest.permission.CAMERA)

    fun allCameraPermissionsGranted() = requiredCameraPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermissions(){
/*        ActivityCompat.requestPermissions(
            context, requiredCameraPermissions, REQUEST_CODE_PERMISSIONS
        )*/
    }
/*
    private fun getCurrentActivity(): Activity {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.appTasks[0].taskInfo.topActivity
        } else {
            am.getRunningTasks(1)[0].topActivity
        }
    }*/
}