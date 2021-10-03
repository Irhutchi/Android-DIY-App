package ie.wit.doityourself.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.doityourself.databinding.ActivityDiyBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber
import timber.log.Timber.i

class DIYActivity : AppCompatActivity() {

    // ActivityDiyBinding augmented class needed to access diff View
    // objects on a particular layout
    private lateinit var binding: ActivityDiyBinding
    var task = DIYModel()
    lateinit var app: MainApp // ref to mainApp object (1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflate layout using binding class
        binding = ActivityDiyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp    // initialise mainApp (2)
        i("DIY Activity started...")

        binding.btnAdd.setOnClickListener() {
            task.title = binding.taskTitle.text.toString()
            task.description = binding.description.text.toString()
            if(task.title.isNotEmpty()) {
                i("add Button Pressed: $task.title")
                app.tasks.add(task.copy())    // use mainApp (3)
                for (i in app.tasks.indices) {
                    i("Diy Job[$i]: ${this.app.tasks[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}