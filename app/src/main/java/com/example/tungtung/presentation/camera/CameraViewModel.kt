package com.example.tungtung.presentation.camera

import androidx.lifecycle.ViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by josephmagara on 9/10/20.
 */

class CameraViewModel : ViewModel() {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }

}