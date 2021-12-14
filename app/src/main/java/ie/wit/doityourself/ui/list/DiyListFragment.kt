package ie.wit.doityourself.ui.list


import android.os.Bundle
import android.util.Log
import android.view.*
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
import timber.log.Timber


class DiyListFragment : Fragment(), DIYClickListener {


    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentDiyListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var diyListViewModel:  DIYListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _fragBinding = FragmentDiyListBinding.inflate(inflater, container, false)
        val view = fragBinding.root

        // setting up linear layout manager to display list of diy tasks

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        Timber.i("Called ViewModelProvider.get")
        diyListViewModel = ViewModelProvider(this).get(DIYListViewModel::class.java)
        // observe public live data, when tasks changes we call render.
        diyListViewModel.observableTaskList.observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let { render(tasks) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = DiyListFragmentDirections.actionDiyListFragmentToDiyFragment()
            findNavController().navigate(action)
        }
        return view
    }

    private fun render(taskList: List<DIYModel>) {
        // create adapter passing in the list of tasks
        fragBinding.recyclerView.adapter = DIYAdapter(taskList, this)
        if (taskList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.noTasksFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.noTasksFound.visibility = View.GONE
        }
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
        val action = DiyListFragmentDirections.actionDiyListFragmentToDiyEditFragment(task.id)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        diyListViewModel.load()

    }

    // Register the callback
//    private fun registerRefreshCallback() {
//        refreshIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { loadDIYTasks() }
//    }
}