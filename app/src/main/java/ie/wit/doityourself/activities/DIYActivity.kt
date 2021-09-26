package ie.wit.doityourself.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.ActivityDiyBinding
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber
import timber.log.Timber.i

class DIYActivity : AppCompatActivity() {

    // ActivityDiyBinding augmented class needed to access diff View
    // objects on a particular layout
    private lateinit var binding: ActivityDiyBinding
    var diy = DIYModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflate layout using binding class
        binding = ActivityDiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_diy)

        Timber.plant(Timber.DebugTree())

        i("DIY Activity Started..")

        binding.btnAdd.setOnClickListener() {
            diy.title = binding.diyTitle.text.toString()
            if (diy.title.isNotEmpty()) {
                i("add Button Pressed: $diy.title")
            }
            else {
                Snackbar
                    .make(it,"Please enter a job title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}