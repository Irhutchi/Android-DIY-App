package ie.wit.doityourself.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.ActivityDiyBinding

class CameraActivity : AppCompatActivity() {

    // ActivityDiyBinding augmented class needed to access diff View
    // objects on a particular layout
    private lateinit var binding: ActivityDiyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiyBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_camera)

        if (allPermissionGranted()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }

    }

    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }



}