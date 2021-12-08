package ie.wit.doityourself.ui.Diy


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.FragmentDiyBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar
import ie.wit.doityourself.helpers.showImagePicker
import androidx.lifecycle.Observer


class DiyFragment : Fragment(), View.OnClickListener {

    // ActivityDiyBinding augmented class needed to access diff View of
    // objects on a particular layout

    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentDiyBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
//    lateinit var navController: NavController
    private lateinit var diyViewModel: DiyViewModel
    var task = DIYModel()

    var edit = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        app = activity?.application as MainApp
//        Timber.i("DIY Activity started...")
        setHasOptionsMenu(true)
//        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)
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

//        val editTask = activity?.intent!!.extras!!.getString("task_edit")
        if (activity?.intent!!.hasExtra("task_edit")) {
            edit = true
            task = activity?.intent!!.extras?.getParcelable("task_edit")!!
            fragBinding.taskTitle.setText(task.title)
            fragBinding.description.setText(task.description)
            fragBinding.btnAdd.setText(R.string.save_task)
            fragBinding.btnPhoto.setText(R.string.take_photo)
            Picasso.get()
                .load(task.image)
                .into(fragBinding.taskImage)
            if (task.image != Uri.EMPTY) {
                fragBinding.chooseImage.setText(R.string.change_task_image)
            }
        }

        fragBinding.btnAdd.setOnClickListener {
            task.title = fragBinding.taskTitle.text.toString()
            task.description = fragBinding.description.text.toString()

            val rgRating: String = if (fragBinding.rgRating.checkedRadioButtonId == R.id.easyBtn) {
                "Easy"
            } else if(fragBinding.rgRating.checkedRadioButtonId == R.id.hardBtn) {
                "Hard"
            } else "Very Hard"
            task.rating = rgRating
            Timber.i("Difficulty Rating $rgRating")

            if(task.title.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_diyTask_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
//                    app.tasks.update(task.copy())
                    findNavController().navigate(R.id.diyListFragment)

                } else {
                    task.copy()
//                    app.tasks.create(task.copy()) // use mainApp (3)
                    Timber.i("add Button Pressed: $task.title")
//                    navController.navigate(R.id.diyListFragment)
                }
//                navController.navigate(R.id.diyListFragment)
            }
        }
        fragBinding.chooseImage.setOnClickListener {
            Timber.i("Select image")
            showImagePicker(imageIntentLauncher)    // trigger the image picker
        }

        fragBinding.btnPhoto.setOnClickListener {
            Timber.i("Take Photo")
//            navController.navigate(R.id.action_diyFragment_to_cameraFragment)
        }
        return view
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to task list
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.createTaskError), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_diytask, menu)
        if (edit) menu.getItem(1).isVisible = true
//        return super.onCreateOptionsMenu(menu)
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
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            // Only recovering uri when the result Code is RESULT_OK
                            task.image = result.data!!.data!!
                            Picasso.get()
                                .load(task.image)
                                .into(fragBinding.taskImage)
                            // when an image is changed, also change the label
                            fragBinding.chooseImage.setText(R.string.change_task_image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                    }
                    else -> { }
                }
            }
    }

}