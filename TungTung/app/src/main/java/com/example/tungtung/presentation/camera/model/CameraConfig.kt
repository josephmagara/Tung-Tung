package com.example.tungtung.presentation.camera.model

import androidx.camera.core.CameraSelector
import java.util.concurrent.Executor

/**
 * Created by josephmagara on 13/10/20.
 */
data class CameraConfig(
    val selector: CameraSelector,
    val executor: Executor
)