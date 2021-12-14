package ie.wit.doityourself.ui.diy

/* View model survives config changes, so is a good place for data that needs to survive */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.doityourself.models.DIYManager
import ie.wit.doityourself.models.DIYModel

class DiyViewModel: ViewModel() {
    // tracks individual task
    private var task = MutableLiveData<DIYModel>()
    // expose public read-only diy task
    var observableDiyTask: MutableLiveData<DIYModel>
        get() = task
        set(value) {task.value = value.value}

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    fun addDiyTask(task: DIYModel) {
        status.value = try {
            DIYManager.create(task)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getDiyTask(id: Long) {
        task.value = DIYManager.findById(id)
    }

}