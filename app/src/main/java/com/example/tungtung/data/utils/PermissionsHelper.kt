package com.example.tungtung.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Created by josephmagara on 9/10/20.
 */
class PermissionsHelper(private val context: Context) {

    private val requiredCameraPermissions = arrayOf(Manifest.permission.CAMERA)

    fun allCameraPermissionsGranted() = requiredCameraPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}