package com.example.tungtung.domain.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.example.tungtung.data.utils.faceDetector
import com.google.mlkit.vision.common.InputImage

/**
 * Created by josephmagara on 13/10/20.
 */
class AnalyseImageUseCase {

    @SuppressLint("UnsafeExperimentalUsageError")
    fun analyse(imageProxy: ImageProxy){
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            faceDetector.process(image)
                .addOnSuccessListener {

                }.addOnFailureListener {

                }
        }
    }
}