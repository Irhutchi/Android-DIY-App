package ie.wit.doityourself.models

interface DIYStore {
    fun findAll(): List<DIYModel>
    fun create(task: DIYModel)
    fun update(task: DIYModel)
}