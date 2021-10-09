package ie.wit.doityourself.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.doityourself.R
import ie.wit.doityourself.adapters.DIYAdapter
import ie.wit.doityourself.adapters.DIYListener
import ie.wit.doityourself.databinding.ActivityDiyListBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel

class DIYListActivity : AppCompatActivity(), DIYListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityDiyListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // In order to present the toolbar - we must explicitly enable it
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        // Retrieving and storing a reference to the MainApp object (for future use!)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = DIYAdapter(app.tasks)
        binding.recyclerView.adapter = DIYAdapter(app.tasks.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // menu event handler - if the event is item_add, we start the DIYListActivity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                // expose intent to permit activity to be launched
                val launcherIntent = Intent(this, DIYActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDIYClick(task: DIYModel) {
        val launcherIntent = Intent(this, DIYActivity::class.java)
        // passing the task to the actvity, enabled via the parcelable mechanism
        launcherIntent.putExtra("task_edit", task)
        startActivityForResult(launcherIntent,0)
    }

    // This is another lifecycle event. This func is triggered when an activity
    // we have started finishes.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
