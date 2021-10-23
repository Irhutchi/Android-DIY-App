package ie.wit.doityourself.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.ActivityCameraBinding
import ie.wit.doityourself.databinding.ActivityDiyBinding
import ie.wit.doityourself.main.MainApp
import java.lang.Exception

class CameraActivity : AppCompatActivity() {

    lateinit var app: MainApp
    // ActivityDiyBinding augmented class needed to access diff View
    // objects on a particular layout
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture:ImageCapture?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieving and storing a reference to the MainApp object
        app = this.application as MainApp

        if (allPermissionGranted()) {
            startCamera()
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider

                    )
                }
            imageCapture = ImageCapture.Builder()
                .build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle (
                    this, cameraSelector,
                    preview, imageCapture
                )

            } catch (err: Exception) {
                Log.d(Constants.TAG, "startCamera Fail:", err)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()){
                startCamera()
            }else {
                // display toast to user indicating current user permissions
                Toast.makeText(this, "Permission not granted by the user", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }


    // Request permission to access camera
    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }



}