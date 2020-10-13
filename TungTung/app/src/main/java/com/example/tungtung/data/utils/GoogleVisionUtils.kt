package com.example.tungtung.data.utils

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

/**
 * Created by josephmagara on 13/10/20.
 */

// Real-time contour detection
private val realTimeFaceDetectionOpts = FaceDetectorOptions.Builder()
    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
    .build()

val faceDetector = FaceDetection.getClient(realTimeFaceDetectionOpts)