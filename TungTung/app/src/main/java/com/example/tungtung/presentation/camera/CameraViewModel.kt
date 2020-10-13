package com.example.tungtung.presentation.camera

import androidx.camera.core.CameraSelector
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tungtung.R
import com.example.tungtung.data.utils.PermissionsHelper
import com.example.tungtung.presentation.camera.model.ViewOpacityTransition
import com.example.tungtung.presentation.camera.model.ViewOpacityTransition.Companion.FULL_OPACITY
import com.example.tungtung.presentation.camera.model.ViewOpacityTransition.Companion.HALF_OPACITY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by josephmagara on 9/10/20.
 */

class CameraViewModel @ViewModelInject constructor(private val permissionsHelper: PermissionsHelper) :
    ViewModel() {

    companion object {
        private const val BUTTON_FADE_DELAY = 4000L
    }

    private val rotateCamera = MutableLiveData<CameraSelector>()
    private val openCamera = MutableLiveData<CameraSelector>()
    private val rotateButtonOpacityTransition: MutableLiveData<ViewOpacityTransition> = MutableLiveData<ViewOpacityTransition>()
    private val permissionsNotGranted: MutableLiveData<Any> = MutableLiveData<Any>()
    private var currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var buttonFadeJob: Job? = null

    private val fadeInTransition = ViewOpacityTransition(R.anim.camera_button_fade_in, FULL_OPACITY)
    private val fadeOutTransition = ViewOpacityTransition(R.anim.camera_button_fade_out, HALF_OPACITY)

    init {
        buttonFadeJob = viewModelScope.launch {
            delay(BUTTON_FADE_DELAY)
            rotateButtonOpacityTransition.value = getTransition(false)
        }
        ensureAllCameraPermissionsAreGranted()
    }

    fun permissionRequestCompleted() {
        if (permissionsHelper.allCameraPermissionsGranted()) {
            openCamera.value = currentCameraSelector
        } else {
            permissionsNotGranted.value = true
        }
    }

    fun onRotateCameraClicked(){
        if (!permissionsHelper.allCameraPermissionsGranted()) return
        currentCameraSelector = if (currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        updateRotateButtonOpacity()
        rotateCamera.value = currentCameraSelector
    }

    fun openCamera(): LiveData<CameraSelector> = openCamera

    fun rotateCamera(): LiveData<CameraSelector> = rotateCamera

    fun onPermissionNotGranted(): LiveData<Any> = permissionsNotGranted

    fun rotateButtonOpacity(): LiveData<ViewOpacityTransition> = rotateButtonOpacityTransition

    private fun ensureAllCameraPermissionsAreGranted() {
        if (!permissionsHelper.allCameraPermissionsGranted()) {
            permissionsHelper.requestCameraPermissions()
        } else {
            openCamera.value = currentCameraSelector
        }
    }

    private fun updateRotateButtonOpacity(){
        rotateButtonOpacityTransition.value = getTransition(true)

        buttonFadeJob?.cancel()
        buttonFadeJob = viewModelScope.launch {
            delay(BUTTON_FADE_DELAY)
            rotateButtonOpacityTransition.value = getTransition(false)
        }
    }

    private fun getTransition(fadeIn: Boolean): ViewOpacityTransition {
        return if (fadeIn){
            fadeInTransition
        } else {
            fadeOutTransition
        }
    }
}