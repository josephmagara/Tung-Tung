package com.example.tungtung.presentation.camera

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

class CameraViewModel @ViewModelInject constructor(private val permissionsHelper: PermissionsHelper) : ViewModel() {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private val openCamera = MutableLiveData<Any>()
    private val permissionsNotGranted: MutableLiveData<Any> = MutableLiveData<Any>()

    init {
        ensureAllCameraPermissionsAreGranted()
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }

    fun permissionRequestCompleted() {
        if (permissionsHelper.allCameraPermissionsGranted()) {
            openCamera.value = true
        } else {
            permissionsNotGranted.value = true
        }
    }

    fun openCamera(): LiveData<Any> = openCamera

    fun onPermissionNotGranted(): LiveData<Any> = permissionsNotGranted

    private fun ensureAllCameraPermissionsAreGranted(){
        if(!permissionsHelper.allCameraPermissionsGranted()){
            permissionsHelper.requestCameraPermissions()
        }
    }
}