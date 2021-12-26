package ie.wit.doityourself.ui.list


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.doityourself.ui.auth.LoggedInViewModel
import ie.wit.doityourself.utils.*
import timber.log.Timber


class DiyListFragment : Fragment(), DIYClickListener {


    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentDiyListBinding? = null
    private val fragBinding get() = _fragBinding!!
//    private lateinit var diyListViewModel:  DIYListViewModel
    lateinit var loader : AlertDialog
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val diyListViewModel: DIYListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _fragBinding = FragmentDiyListBinding.inflate(inflater, container, false)
        loader = createLoader(requireActivity())
        val view = fragBinding.root

        // setting up linear layout manager to display list of diy tasks

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener{
            val action = DiyListFragmentDirections.actionDiyListFragmentToDiyFragment()
            findNavController().navigate(action)
        }
        showLoader(loader,"Downloading Diy List")

        diyListViewModel.observableTaskList.observe(viewLifecycleOwner, Observer {
                tasks ->
            tasks?.let { render(tasks as ArrayList<DIYModel>) }
            hideLoader(loader)
            checkSwipeRefresh()
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Task")
                val adapter = fragBinding.recyclerView.adapter as DIYAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                diyListViewModel.delete(diyListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as DIYModel).uid!!)
                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onDIYClick(viewHolder.itemView.tag as DIYModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)


        return view
    }

    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Diy List")
            if(!diyListViewModel.readOnly.value!!) {
                diyListViewModel.loadAll()
            } else
                diyListViewModel.load()
        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    private fun render(taskList: ArrayList<DIYModel>) {
        // create adapter passing in the list of tasks
        fragBinding.recyclerView.adapter = DIYAdapter(taskList, this,
                                        diyListViewModel.readOnly.value!!)
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

        val item = menu.findItem(R.id.toggleDonations) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleDonations: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleDonations.isChecked = false

        toggleDonations.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) diyListViewModel.loadAll()
            else diyListViewModel.load()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onDIYClick(task: DIYModel) {
        val action = DiyListFragmentDirections.actionDiyListFragmentToDiyEditFragment(task.uid!!)

        if(!diyListViewModel.readOnly.value!!) {
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Donations")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                diyListViewModel.liveFirebaseUser.value = firebaseUser
                diyListViewModel.load()
            }
        })

    }



    // Register the callback
//    private fun registerRefreshCallback() {
//        refreshIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { loadDIYTasks() }
//    }
}