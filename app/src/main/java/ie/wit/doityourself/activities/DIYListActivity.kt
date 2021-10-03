package ie.wit.doityourself.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.ActivityDiyListBinding
import ie.wit.doityourself.databinding.CardDiytaskBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel

class DIYListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityDiyListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        // Retrieving and storing a reference to the MainApp object (for future use!)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = DIYAdapter(app.tasks)
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
}

class DIYAdapter constructor(private var tasks: List<DIYModel>) :
    RecyclerView.Adapter<DIYAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDiytaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[holder.adapterPosition]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    class MainHolder(private val binding : CardDiytaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: DIYModel) {
            binding.taskTitle.text = task.title
            binding.description.text = task.description
        }
    }
}
