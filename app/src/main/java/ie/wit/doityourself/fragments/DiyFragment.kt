package ie.wit.doityourself.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.FragmentDiyBinding
import ie.wit.doityourself.main.MainApp
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar
import ie.wit.doityourself.helpers.showImagePicker


/**
 * A simple [Fragment] subclass.
 * Use the [DiyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiyFragment : Fragment(), View.OnClickListener {

    // ActivityDiyBinding augmented class needed to access diff View
    // objects on a particular layout

    lateinit var app: MainApp // ref to mainApp object (1)
    private var _fragBinding: FragmentDiyBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var task = DIYModel()
    var edit = false
    var nav2cam: NavController ?= null
//    var nav2list: NavController ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = activity?.application as MainApp
        Timber.i("DIY Activity started...")
        setHasOptionsMenu(true)

        registerImagePickerCallback()   // initialise the image picker callback func.

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _fragBinding = FragmentDiyBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        getString(R.string.action_diy).also { activity?.title = it }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    app.diyStore.update(task.copy())
                } else {
                    app.diyStore.create(task.copy()) // use mainApp (3)
                    Timber.i("add Button Pressed: $task.title")
                }
                getActivity()?.setResult(Activity.RESULT_OK)
                getActivity()?.finish()
            }
        }
        fragBinding.chooseImage.setOnClickListener {
            Timber.i("Select image")
            showImagePicker(imageIntentLauncher)    // trigger the image picker
        }

//        nav2list=Navigation.findNavController(view)
//        view.findViewById<Button>(R.id.btnAdd)?.setOnClickListener(this)

        nav2cam=Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnPhoto)?.setOnClickListener(this)


//        fragBinding.btnPhoto.setOnClickListener {
//            Timber.i("Take Photo")
//            val nextFrag = CameraFragment()
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.btnPhoto, nextFrag, "findThisFragment")
//                .addToBackStack(null)
//                .commit()
//        }
    }

    override fun onClick(v: View?) {
        nav2cam?.navigate(R.id.action_diyFragment_to_cameraFragment)
//        nav2list?.navigate(R.id.action_diyFragment_to_diyListFragment)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_diytask, menu)
        if (edit) menu.getItem(1).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
        requireView().findNavController()) || super.onOptionsItemSelected(item)
//        when (item.itemId) {
//            R.id.item_cancel -> { activity?.finish()
//                Timber.i("Edit task aborted")
//            }
//            R.id.item_delete -> {
//                app.diyStore.delete(task)
//                activity?.finish()
//                Timber.i("delete task button pressed")
//            }
//        }
//        return super.onOptionsItemSelected(item)
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
                    else -> {
                    }
                }
            }
    }

}