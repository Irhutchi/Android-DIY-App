package ie.wit.doityourself.models

import androidx.lifecycle.MutableLiveData

interface DIYStore {
    fun findAll(): List<DIYModel>
//    fun findAll(taskList: MutableLiveData<List<DIYModel>>)
    fun findById(id: Long) : DIYModel?
    fun create(task: DIYModel)
    fun update(task: DIYModel)
    fun delete(task: DIYModel)
}