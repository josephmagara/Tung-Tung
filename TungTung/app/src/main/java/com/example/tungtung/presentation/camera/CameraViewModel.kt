package com.example.tungtung.presentation.camera

import android.os.CountDownTimer
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tungtung.R
import com.example.tungtung.data.utils.CameraHelper
import com.example.tungtung.data.utils.PermissionsHelper
import com.example.tungtung.domain.camera.AnalyseImageUseCase
import com.example.tungtung.presentation.camera.model.CameraConfig
import com.example.tungtung.presentation.camera.model.ViewOpacityTransition
import com.example.tungtung.presentation.camera.model.ViewOpacityTransition.Companion.FULL_OPACITY
import com.example.tungtung.presentation.camera.model.ViewOpacityTransition.Companion.HALF_OPACITY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by josephmagara on 9/10/20.
 */

class CameraViewModel @ViewModelInject constructor(
    cameraHelper: CameraHelper,
    private val permissionsHelper: PermissionsHelper,
    private val analyseImageUseCase: AnalyseImageUseCase
) : ViewModel() {

    companion object {
        private const val BUTTON_FADE_DELAY = 4000L
    }

    private val rotateCamera = MutableLiveData<CameraConfig>()
    private val openCamera = MutableLiveData<CameraConfig>()
    private val rotateButtonOpacityTransition: MutableLiveData<ViewOpacityTransition> =
        MutableLiveData<ViewOpacityTransition>()
    private val permissionsNotGranted: MutableLiveData<Any> = MutableLiveData<Any>()
    private val countDownTimer: MutableLiveData<Int> = MutableLiveData<Int>()


    private var buttonFadeJob: Job? = null
    private var currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private val fadeInTransition = ViewOpacityTransition(R.anim.camera_button_fade_in, FULL_OPACITY)
    private val fadeOutTransition =
        ViewOpacityTransition(R.anim.camera_button_fade_out, HALF_OPACITY)
    private val executor = cameraHelper.getCameraExecutor()

    init {
        buttonFadeJob = viewModelScope.launch {
            delay(BUTTON_FADE_DELAY)
            rotateButtonOpacityTransition.value = getTransition(false)
        }

        ensureAllCameraPermissionsAreGranted()
    }

    fun permissionRequestCompleted() {
        if (permissionsHelper.allCameraPermissionsGranted()) {
            openCamera.value = CameraConfig(currentCameraSelector, executor)
        } else {
            permissionsNotGranted.value = true
        }
    }

    fun onRotateCameraClicked() {
        if (!permissionsHelper.allCameraPermissionsGranted()) return
        currentCameraSelector = if (currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        updateRotateButtonOpacity()
        rotateCamera.value = CameraConfig(currentCameraSelector, executor)
    }

    fun openCamera(): LiveData<CameraConfig> = openCamera

    fun rotateCamera(): LiveData<CameraConfig> = rotateCamera

    fun onPermissionNotGranted(): LiveData<Any> = permissionsNotGranted

    fun rotateButtonOpacity(): LiveData<ViewOpacityTransition> = rotateButtonOpacityTransition

    fun countDownTimer(): LiveData<Int> = countDownTimer

    fun onNewImage(imageProxy: ImageProxy) = analyseImageUseCase.analyse(imageProxy)

    fun countDownTimerClicked() {
        if (countDownTimer.value == 0) {
            restartCountDownTimer()
        }
    }

    private fun restartCountDownTimer() {
        startCountDownTimer()
    }

    private fun ensureAllCameraPermissionsAreGranted() {
        if (!permissionsHelper.allCameraPermissionsGranted()) {
            permissionsHelper.requestCameraPermissions()
        } else {
            openCamera.value = CameraConfig(currentCameraSelector, executor)
            startCountDownTimer()
        }
    }

    private fun startCountDownTimer() {
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                countDownTimer.value = seconds.toInt()
            }

            override fun onFinish() {}
        }
        timer.start()
    }

    private fun updateRotateButtonOpacity() {
        rotateButtonOpacityTransition.value = getTransition(true)

        buttonFadeJob?.cancel()
        buttonFadeJob = viewModelScope.launch {
            delay(BUTTON_FADE_DELAY)
            rotateButtonOpacityTransition.value = getTransition(false)
        }
    }

    private fun getTransition(fadeIn: Boolean): ViewOpacityTransition {
        return if (fadeIn) {
            fadeInTransition
        } else {
            fadeOutTransition
        }
    }
}