package ie.wit.doityourself.ui.diy


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.FragmentDiyBinding
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar
import ie.wit.doityourself.helpers.showImagePicker
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ie.wit.doityourself.DiyEditFragmentArgs
import ie.wit.doityourself.ui.auth.LoggedInViewModel
import ie.wit.doityourself.ui.list.DIYListViewModel


class DiyFragment : Fragment(), View.OnClickListener {

    private var _fragBinding: FragmentDiyBinding? = null
    private val fragBinding get() = _fragBinding!!
    //private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var diyViewModel: DiyViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val diyListViewModel: DIYListViewModel by activityViewModels()
//    private val args by navArgs<Diy>()
    var task = DIYModel()

    var edit = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("DIY Fragment started...")
        setHasOptionsMenu(true)
//        registerImagePickerCallback()   // initialise the image picker callback func.

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragBinding = FragmentDiyBinding.inflate(inflater, container, false)
        val view = fragBinding.root
        getString(R.string.action_diy).also { activity?.title = it }

        diyViewModel = ViewModelProvider(this).get(DiyViewModel::class.java)
        diyViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })

        addNewTaskButtonListener(fragBinding)

        fragBinding.btnPhoto.setOnClickListener {
            Timber.i("Take Photo")
            val action = DiyFragmentDirections.actionDiyFragmentToCameraFragment()
            findNavController().navigate(action)
        }
        return view
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to task list
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.createTaskError), Toast.LENGTH_LONG).show()
        }
    }

    fun addNewTaskButtonListener(layout: FragmentDiyBinding) {
        layout.btnAdd.setOnClickListener {
            val title = fragBinding.taskTitle.text.toString()
            val description = fragBinding.description.text.toString()

            val rgRating: String = if (fragBinding.rgRating.checkedRadioButtonId == R.id.easyBtn) {
                "Easy"
            } else if(fragBinding.rgRating.checkedRadioButtonId == R.id.hardBtn) {
                "Hard"
            } else "Very Hard"
            val rating = rgRating
            Timber.i("Difficulty Rating $rgRating")

            if(title.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_diyTask_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                diyViewModel.addDiyTask(loggedInViewModel.liveFirebaseUser, DIYModel(title = title,
                    description = description, rating = rating,
                    email = loggedInViewModel.liveFirebaseUser.value?.email!!))
                Timber.i("add Button Pressed: $task.title")
//                findNavController().navigate(R.id.diyListFragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_diytask, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
        requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        findNavController().navigate(R.id.cameraFragment)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            DiyFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
//        diyViewModel.getDiyTask(args.taskid)
    }


//    private fun registerImagePickerCallback() {
//        imageIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { result ->
//                when (result.resultCode) {
//                    AppCompatActivity.RESULT_OK -> {
//                        if (result.data != null) {
//                            Timber.i("Got Result ${result.data!!.data}")
//                            // Only recovering uri when the result Code is RESULT_OK
//                            task.image = result.data!!.data!!
//                            Picasso.get()
//                                .load(task.image)
//                                .into(fragBinding.taskImage)
//                            // when an image is changed, also change the label
//                            fragBinding.chooseImage.setText(R.string.change_task_image)
//                        }
//                    }
//                    AppCompatActivity.RESULT_CANCELED -> {
//                    }
//                    else -> { }
//                }
//            }
//    }

}


