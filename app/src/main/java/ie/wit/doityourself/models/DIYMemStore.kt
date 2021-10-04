package ie.wit.doityourself.models

import timber.log.Timber.i

class DIYMemStore: DIYStore {

    val tasks = ArrayList<DIYModel>()

    override fun findAll(): List<DIYModel> {
        return tasks
    }

    override fun create(task: DIYModel) {
        tasks.add(task)
        logAll()
    }

    fun logAll() {
        tasks.forEach{ i("${it}")}
    }
}