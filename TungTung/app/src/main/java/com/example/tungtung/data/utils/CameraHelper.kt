package com.example.tungtung.data.utils

import android.content.Context
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import javax.inject.Inject

/**
 * Created by josephmagara on 13/10/20.
 */
class CameraHelper @Inject constructor(private val context: Context)  {

    fun getCameraExecutor(): Executor = ContextCompat.getMainExecutor(context)
}