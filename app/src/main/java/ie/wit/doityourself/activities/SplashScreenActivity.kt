package ie.wit.doityourself.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import ie.wit.doityourself.R

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this,DIYListActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // set delay 3sec before opening DIYActivity
    }
}