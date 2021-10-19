package ie.wit.doityourself.models

import timber.log.Timber
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}


class DIYMemStore: DIYStore {

    val tasks = ArrayList<DIYModel>()

    override fun findAll(): List<DIYModel> {
        return tasks
    }

    override fun create(task: DIYModel) {
        task.id = getId()
        tasks.add(task)
        logAll()
    }

    fun update(task: DIYModel) {
        var foundTask: DIYModel? = tasks.find { t -> t.id == task.id }
        if (foundTask != null) {
            foundTask.title = task.title
            foundTask.description = task.description
            foundTask.image = task.image
            logAll()
        }
    }

    fun logAll() {
        Timber.v("** DIY List **")
        tasks.forEach{ i("${it}")}
    }
}