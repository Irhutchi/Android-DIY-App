package ie.wit.doityourself.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.doityourself.models.DIYManager
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber

class DIYListViewModel : ViewModel() {

    // raw data accessing the diy model list
    private val taskList = MutableLiveData<List<DIYModel>>()

    // expose the diy task list with get accessor
    val observableTaskList: LiveData<List<DIYModel>>
        get() = taskList

    // call load func when we initialise
    init { load()
        Timber.i("DiyViewModel created!")
    }

    fun load() {
        try {
            taskList.value = DIYManager.findAll()
            Timber.i("Retrofit Success : $taskList.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }


//    fun delete(id: Long) {
//        try {
//            DIYManager.delete(task = id)
//            Timber.i("Retrofit Delete Success")
//        }
//        catch (e: Exception) {
//            Timber.i("Retrofit Delete Error : $e.message")
//        }
//    }

    // clean up resources when view model is detached or finished.
//    override fun OnCleared() {
//        super.onCleared()
//        Timber.i("DiyListViewModel destroyed!")
//    }
}