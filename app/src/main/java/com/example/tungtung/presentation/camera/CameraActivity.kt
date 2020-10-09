package com.example.tungtung.presentation.camera

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tungtung.R

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    lateinit var cameraViewModel: CameraViewModel

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