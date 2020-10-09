package com.example.tungtung.presentation.camera

import androidx.camera.core.CameraSelector
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tungtung.data.utils.PermissionsHelper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by josephmagara on 9/10/20.
 */

class CameraViewModel @ViewModelInject constructor(private val permissionsHelper: PermissionsHelper) :
    ViewModel() {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private val rotateCamera = MutableLiveData<CameraSelector>()
    private val openCamera = MutableLiveData<CameraSelector>()
    private val permissionsNotGranted: MutableLiveData<Any> = MutableLiveData<Any>()
    private var currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    init {
        ensureAllCameraPermissionsAreGranted()
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }

    fun permissionRequestCompleted() {
        if (permissionsHelper.allCameraPermissionsGranted()) {
            openCamera.value = currentCameraSelector
        } else {
            permissionsNotGranted.value = true
        }
    }

    fun onRotateCameraClicked(){
        currentCameraSelector = if (currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        rotateCamera.value = currentCameraSelector
    }

    fun openCamera(): LiveData<CameraSelector> = openCamera

    fun rotateCamera(): LiveData<CameraSelector> = rotateCamera

    fun onPermissionNotGranted(): LiveData<Any> = permissionsNotGranted


    private fun ensureAllCameraPermissionsAreGranted() {
        if (!permissionsHelper.allCameraPermissionsGranted()) {
            permissionsHelper.requestCameraPermissions()
        } else {
            openCamera.value = currentCameraSelector
        }
    }
}