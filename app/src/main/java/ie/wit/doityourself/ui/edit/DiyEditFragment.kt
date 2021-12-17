package ie.wit.doityourself

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ie.wit.doityourself.databinding.DiyEditFragmentBinding
import ie.wit.doityourself.ui.edit.DiyEditViewModel
import timber.log.Timber

class DiyEditFragment: Fragment() {

    private var _fragBinding: DiyEditFragmentBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var diyEditViewModel: DiyEditViewModel
    private val args by navArgs<DiyEditFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = DiyEditFragmentBinding.inflate(inflater, container, false)
        val view = fragBinding.root
        getString(R.string.edit_diy).also { activity?.title = it }

        diyEditViewModel = ViewModelProvider(this).get(DiyEditViewModel::class.java)
        diyEditViewModel.observableDiyTask.observe(viewLifecycleOwner, Observer { render() })
        diyEditViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { renderStatus(status) }
        })

        fragBinding.editTaskButton.setOnClickListener {
            Timber.i("EDIT TASK ${fragBinding.diytaskvm?.observableDiyTask!!.value!!}")
            diyEditViewModel.editTask(fragBinding.diytaskvm?.observableDiyTask!!.value!!)
        }
        fragBinding.deleteTaskButton.setOnClickListener {
            Timber.i("DELETE TASK")
            diyEditViewModel.deleteTask(fragBinding.diytaskvm?.observableDiyTask!!.value!!)
        }


        return view
    }
    private fun renderStatus(status : Boolean) {
        when (status) {
            true -> {
                view?.let {
                    findNavController().popBackStack()
                }
            }
            false -> {}
        }
    }

    private fun render() {
        fragBinding.diytaskvm = diyEditViewModel
    }

    override fun onResume() {
        super.onResume()
        diyEditViewModel.getDiyTask(args.taskid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}