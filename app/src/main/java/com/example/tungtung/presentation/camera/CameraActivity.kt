package com.example.tungtung.presentation.camera

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tungtung.R
import com.example.tungtung.data.utils.PermissionsHelper.Companion.REQUEST_CODE_PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private val cameraViewModel by viewModels<CameraViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraViewModel.openCamera().observe(this, {
            startCamera()
        })

        cameraViewModel.onPermissionNotGranted().observe(this, {
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            cameraViewModel.permissionRequestCompleted()
        }
    }

    private fun startCamera() {

    }
}