package com.example.tungtung.data.utils

import com.google.mlkit.vision.face.FaceDetectorOptions

/**
 * Created by josephmagara on 13/10/20.
 */

// High-accuracy landmark detection and face classification
val highAccuracyFaceDetectionOpts = FaceDetectorOptions.Builder()
    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
    .build()

// Real-time contour detection
val realTimeFaceDetectionOpts = FaceDetectorOptions.Builder()
    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
    .build()