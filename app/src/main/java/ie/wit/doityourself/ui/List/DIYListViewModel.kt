package ie.wit.doityourself.ui.List

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.doityourself.models.DIYManager
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber

class DIYListViewModel : ViewModel() {

    private val taskList = MutableLiveData<List<DIYModel>>()

    val observableTaskList: LiveData<List<DIYModel>>
        get() = taskList

    init {
        load()
    }

    fun load() {
        try {
            DIYManager.findAll()
            Timber.i("Retrofit Success : $taskList.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
//        taskList.value = DIYManager.findAll()
    }
}