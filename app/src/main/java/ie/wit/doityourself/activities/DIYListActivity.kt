package ie.wit.doityourself.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.wit.doityourself.R
import ie.wit.doityourself.main.MainApp

class DIYListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diy_list)
        // Retrieving and storing a reference to the MainApp object (for future use!)
        app = application as MainApp
    }
}