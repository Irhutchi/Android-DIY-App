package ie.wit.doityourself.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.doityourself.firebase.FirebaseDBManager
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber

class DIYListViewModel : ViewModel() {

    // raw data accessing the diy model list
    private val taskList = MutableLiveData<List<DIYModel>>()

    var readOnly = MutableLiveData(false)

    // expose the diy task list with get accessor
    val observableTaskList: LiveData<List<DIYModel>>
        get() = taskList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    // call load func when we initialise
    init { load()
        Timber.i("DiyViewModel created!")
    }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, taskList)
            Timber.i("Diy Task Load Success : ${taskList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }


    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid, id)
            Timber.i("Delete Success of $id by $userid")
        }
        catch (e: Exception) {
            Timber.i("Delete Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(taskList)
            Timber.i("LoadAll Success : ${taskList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("LoadAll Error : $e.message")
        }
    }

    // clean up resources when view model is detached or finished.
//    override fun OnCleared() {
//        super.onCleared()
//        Timber.i("DiyListViewModel destroyed!")
//    }
}