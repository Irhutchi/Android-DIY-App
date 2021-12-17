package ie.wit.doityourself.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.doityourself.models.DIYManager
import ie.wit.doityourself.models.DIYModel

class DiyEditViewModel : ViewModel() {
    // tracks individual task
    private var task = MutableLiveData<DIYModel>()
    // expose public read-only diy task
    var observableDiyTask: MutableLiveData<DIYModel>
        get() = task
        set(value) {task.value = value.value}

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    fun getDiyTask(id: Long) {
        task.value = DIYManager.findById(id)
    }
    fun editTask(diyTaskModel: DIYModel){
        status.value = try {
            DIYManager.update(diyTaskModel)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    fun deleteTask(diyTaskModel: DIYModel){
        status.value = try {
            DIYManager.delete(diyTaskModel)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}