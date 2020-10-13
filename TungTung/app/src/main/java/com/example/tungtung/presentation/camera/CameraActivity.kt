package com.example.tungtung.presentation.camera

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.tungtung.R
import com.example.tungtung.data.utils.PermissionsHelper.Companion.REQUEST_CODE_PERMISSIONS
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
            val opacityTransition: Animation = AnimationUtils.loadAnimation(applicationContext, it.transitionId)
            opacityTransition.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    rotate_camera_button.background.alpha = it.endOpacity
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
            rotate_camera_button.startAnimation(opacityTransition)
        })

        cameraViewModel.onPermissionNotGranted().observe(this, {
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            finish()
        })

        rotate_camera_button.setOnClickListener {
            cameraViewModel.onRotateCameraClicked()
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

    private fun startCamera(cameraSelector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

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

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)

            } catch (exc: Exception) {
                Timber.e("Use case binding failed: + $exc")
            }

        }, ContextCompat.getMainExecutor(this))
    }
}