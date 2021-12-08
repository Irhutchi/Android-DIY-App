package ie.wit.doityourself.ui.Diy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.doityourself.models.DIYManager
import ie.wit.doityourself.models.DIYModel

class DiyViewModel: ViewModel() {

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
}