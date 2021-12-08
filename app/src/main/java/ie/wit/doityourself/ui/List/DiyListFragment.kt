package ie.wit.doityourself.ui.List


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.doityourself.R
import ie.wit.doityourself.adapters.DIYAdapter
import ie.wit.doityourself.adapters.DIYClickListener
import ie.wit.doityourself.databinding.FragmentDiyListBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DiyListFragment : Fragment(), DIYClickListener {
    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentDiyListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var diyListViewModel:  DIYListViewModel
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)

//        registerRefreshCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragBinding = FragmentDiyListBinding.inflate(inflater, container, false)
        val view = fragBinding.root

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        diyListViewModel = ViewModelProvider(this).get(DIYListViewModel::class.java)
        diyListViewModel.observableTaskList.observe(viewLifecycleOwner, Observer {
            tasks ->
            tasks?.let { render(tasks) }
            tasks
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
//            val action = DiyListFragmentDirections.actionDiyListFragmentToDiyFragment()
            findNavController().navigate(R.id.diyFragment)
        }

//        activity?.title = getString(R.string.action_diyList)
//        activity?.title = getString(R.string.action_diyList)
//        loadDIYTasks()
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onDIYClick(task: DIYModel) {
//        val editTask = activity?.intent!!.extras!!.getString("task_edit")
//        val launcherIntent = Intent(this, DiyFragment::class.java)
//        activity?.intent!!.hasExtra("task_edit")
//        val action = DiyListFragmentDirections.actionDiyListFragmentToAboutusFragment()
        findNavController().navigate(R.id.diyFragment)
    }

    private fun render(taskList: List<DIYModel>) {
        fragBinding.recyclerView.adapter = DIYAdapter(taskList, this)
        if (taskList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.noTasksFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.noTasksFound.visibility = View.GONE
        }
    }

//    private fun loadDIYTasks() {
//        showDiyTasks(app.tasks.findAll())
//    }
//
//    fun showDiyTasks (tasks: List<DIYModel>) {
//        fragBinding.recyclerView.adapter = DIYAdapter(tasks, this)
//        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DiyListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        diyListViewModel.load()
//        activity?.title = getString(R.string.action_diyList)
//        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
//        fragBinding.recyclerView.adapter = DIYAdapter(app.tasks.findAll(),this)

    }

    // Register the callback
//    private fun registerRefreshCallback() {
//        refreshIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { loadDIYTasks() }
//    }
}