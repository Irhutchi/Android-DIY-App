package ie.wit.doityourself.models

import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}


object DIYManager: DIYStore {

    private val tasks = ArrayList<DIYModel>()

    override fun findAll() {
//        taskList: MutableLiveData<List<DIYModel>>
//        return tasks
    }

    override fun create(task: DIYModel) {
        task.id = getId()
        tasks.add(task)
        logAll()
    }

//    override fun create(task: DIYModel) {
//        task.id = getId()
//        tasks.add(task)
//        logAll()
//    }

    override fun update(task: DIYModel) {
        var foundTask: DIYModel? = tasks.find { t -> t.id == task.id }
        if (foundTask != null) {
            foundTask.title = task.title
            foundTask.description = task.description
            foundTask.rating = task.rating
            foundTask.image = task.image
            logAll()
        }
    }

    override fun delete(task: DIYModel) {
        val tasksList = findAll() as java.util.ArrayList<DIYModel>
        var foundTask: DIYModel? = tasksList.find { t -> t.id == task.id }
        if (foundTask != null) {
            tasks.remove(task)
            i("Task: ${tasks} removed")
            logAll()
        }
    }

    fun logAll() {
        Timber.v("** DIY List **")
        tasks.forEach{ i("${it}")}
    }
}