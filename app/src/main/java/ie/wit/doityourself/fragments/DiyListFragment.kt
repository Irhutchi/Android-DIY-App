package ie.wit.doityourself.fragments


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.doityourself.R
import ie.wit.doityourself.adapters.DIYAdapter
import ie.wit.doityourself.adapters.DIYListener
import ie.wit.doityourself.databinding.FragmentDiyListBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel


/**
 * A simple [Fragment] subclass.
 * Use the [DiyListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiyListFragment : Fragment(), DIYListener {
    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentDiyListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)

        registerRefreshCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDiyListBinding.inflate(inflater, container, false)
        val view = fragBinding.root
        activity?.title = getString(R.string.action_diyList)
//        activity?.title = getString(R.string.action_diyList)
        loadDIYTasks()
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
//        findNavController().navigate(R.id.diyFragment)
        findNavController().navigate(R.id.action_diyListFragment_to_diyFragment)
    }

    private fun loadDIYTasks() {
        showDiyTasks(app.tasks.findAll())
    }

    fun showDiyTasks (tasks: List<DIYModel>) {
        fragBinding.recyclerView.adapter = DIYAdapter(tasks, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

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
        activity?.title = getString(R.string.action_diyList)
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.recyclerView.adapter = DIYAdapter(app.tasks.findAll(),this)

    }

    // Register the callback
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadDIYTasks() }
    }
}


//        nav2task?.navigate(R.id.action_diyListFragment_to_diyFragment)
//        FragmentDiyBinding activity = (DiyFragment) getActivity()
//        val launcherIntent = Intent(this, DiyFragment::class.java)
//        // passing the task to the actvity, enabled via the parcelable mechanism
//        launcherIntent.putExtra("task_edit", task)
//        refreshIntentLauncher.launch(launcherIntent)