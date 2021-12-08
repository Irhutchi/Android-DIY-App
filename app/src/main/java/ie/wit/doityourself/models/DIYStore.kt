package ie.wit.doityourself.models

import androidx.lifecycle.MutableLiveData

interface DIYStore {
    fun findAll()
    fun create(task: DIYModel)
    fun update(task: DIYModel)
    fun delete(task: DIYModel)
}