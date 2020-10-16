package com.example.tungtung.presentation.camera

import android.os.Bundle
import android.util.Size
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.example.tungtung.R
import com.example.tungtung.data.utils.PermissionsHelper.Companion.REQUEST_CODE_PERMISSIONS
import com.example.tungtung.presentation.camera.model.CameraConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.camera_view.*
import timber.log.Timber


@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private val cameraViewModel by viewModels<CameraViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraViewModel.openCamera().observe(this, {
            startCamera(it)
        })

        cameraViewModel.rotateCamera().observe(this, {
            startCamera(it)
        })

        cameraViewModel.rotateButtonOpacity().observe(this, {
            val opacityTransition: Animation =
                AnimationUtils.loadAnimation(applicationContext, it.transitionId)
            opacityTransition.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    rotate_camera_button.background.alpha = it.endOpacity
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
            rotate_camera_button.startAnimation(opacityTransition)
        })

        cameraViewModel.countDownTimer().observe(this, {
            count_down_ticker.text = "$it"
        })

        cameraViewModel.onPermissionNotGranted().observe(this, {
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            finish()
        })

        rotate_camera_button.setOnClickListener {
            cameraViewModel.onRotateCameraClicked()
        }

        count_down_timer_view.setOnClickListener {
            cameraViewModel.countDownTimerClicked()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            cameraViewModel.permissionRequestCompleted()
        }
    }

    private fun startCamera(cameraConfig: CameraConfig) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val owner: LifecycleOwner = this
        with(cameraConfig) {
            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(camera_view.surfaceProvider)
                    }

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(executor, { image ->
                        cameraViewModel.onNewImage(image)
                    })
                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(owner, selector, imageAnalysis, preview)

                } catch (exc: Exception) {
                    Timber.e("Use case binding failed: + $exc")
                }

            }, executor)
        }
    }
}