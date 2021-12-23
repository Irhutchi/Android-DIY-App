package ie.wit.doityourself.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.doityourself.firebase.FirebaseDBManager
import ie.wit.doityourself.models.DIYModel
import timber.log.Timber

class DiyEditViewModel : ViewModel() {
    // tracks individual task
    private val task = MutableLiveData<DIYModel>()
    // expose public read-only diy task
    var observableDiyTask: MutableLiveData<DIYModel>
        get() = task
        set(value) {task.value = value.value}

//    private val status = MutableLiveData<Boolean>()
//    val observableStatus: LiveData<Boolean>
//        get() = status

    fun getDiyTask(userid: String, id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, task)
            Timber.i("Edit View Render Success : ${
                task.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Edit View Error : $e.message")
        }

    }
    fun editTask(userid:String, id: String, task: DIYModel){
        try {
            FirebaseDBManager.update(userid, id, task)
            Timber.i("update() Success : $task")
            true
        } catch (e: IllegalArgumentException) {
            Timber.i("update() Error : $e.message")
        }
    }
//    fun deleteTask(userid: String, id: DIYModel){
//        status.value = try {
//            FirebaseDBManager.delete(userid, id)
//            true
//        } catch (e: IllegalArgumentException) {
//            false
//        }
//    }
}