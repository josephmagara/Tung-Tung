package com.example.tungtung.domain.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.example.tungtung.data.utils.faceDetector
import com.example.tungtung.domain.face.FaceData
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by josephmagara on 13/10/20.
 */
class AnalyseImageUseCase @Inject constructor(){

    @SuppressLint("UnsafeExperimentalUsageError")
    fun analyse(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    faces.forEach { face ->
                        face.smilingProbability?.let {
                            FaceData(it)
                            Timber.v("Percentage of smile: $it")
                        }
                    }
                }.addOnFailureListener { exception ->
                    Timber.e(exception)
                }
        }
    }
}