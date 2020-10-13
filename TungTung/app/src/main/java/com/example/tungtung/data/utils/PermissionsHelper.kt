package com.example.tungtung.data.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tungtung.application.TungTungApplication
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

    fun requestCameraPermissions() {
        getActivityFromContext(context)?.let {
            ActivityCompat.requestPermissions(it, requiredCameraPermissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun getActivityFromContext(context: Context): Activity? {
        if (context is ContextWrapper) {
            return when (context) {
                is Activity -> {
                    context
                }
                is Application -> {
                    (context as TungTungApplication).getActiveActivity()
                }
                else -> {
                    getActivityFromContext(context.baseContext)
                }
            }
        }
        return null
    }
}