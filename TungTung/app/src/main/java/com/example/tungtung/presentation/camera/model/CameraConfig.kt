package com.example.tungtung.presentation.camera.model

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import java.util.concurrent.Executor

/**
 * Created by josephmagara on 13/10/20.
 */
data class CameraConfig(
    val selector: CameraSelector,
    val imageAnalysis: ImageAnalysis,
    val executor: Executor
)