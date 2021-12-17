package ie.wit.doityourself.ui.camera

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.FragmentCameraBinding
import ie.wit.doityourself.main.MainApp
import timber.log.Timber.i
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {

    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentCameraBinding? = null
    private val fragBinding get() = _fragBinding!!
    private var imageCapture: ImageCapture?=null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = activity?.application as MainApp
        i("Camera Activity started...")
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragBinding = FragmentCameraBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        getString(R.string.action_camera).also { activity?.title = it }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Request camera permissions
        if (allPermissionGranted()) {
            startCamera()
            Toast.makeText(this@CameraFragment.activity,
                "Permission Granted",
                Toast.LENGTH_SHORT).show()
        } else {
            this@CameraFragment.activity?.let {
                ActivityCompat.requestPermissions(
                    it, Constants.REQUIRED_PERMISSIONS,
                    Constants.REQUEST_CODE_PERMISSIONS
                )
            }
        }
        // Set up the listener for take photo button
        fragBinding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.getExternalFilesDir(null)?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir!!
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture?: return
        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory, SimpleDateFormat(Constants.FILE_NAME_FORMAT,
                Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg")
        // Create output options object which contains file + metadata
        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()
        // Set up image capture listener, which is triggered after photo has been taken
        this@CameraFragment.activity?.let { ContextCompat.getMainExecutor(it) }?.let {
            imageCapture.takePicture(
                outputOption, it,
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo Saved"

                        Toast.makeText(this@CameraFragment.activity,
                            "$msg $savedUri",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    override fun onError(exception: ImageCaptureException) {
                        i("onError: ${exception.message}",)
                    }
                }
            )
        }

    }

    private fun startCamera() {

        val cameraProviderFuture = this@CameraFragment.activity?.let {
            ProcessCameraProvider
                .getInstance(it)
        }

        cameraProviderFuture?.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        fragBinding.viewFinder.surfaceProvider

                    )
                }
            imageCapture = ImageCapture.Builder()
                .build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle (
                    this, cameraSelector,
                    preview, imageCapture
                )

            } catch (err: Exception) {
                i("startCamera Fail:")
            }
        }, this@CameraFragment.activity?.let { ContextCompat.getMainExecutor(it) } )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()){
                startCamera()
            }else {
                // display toast to user indicating current user permissions
                Toast.makeText(this@CameraFragment.activity,
                    "Permission not granted by the user",
                    Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }

    }


    // Request permission to access camera
    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireActivity().baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_camera, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}