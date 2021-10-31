package ie.wit.doityourself.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.doityourself.helpers.*
import timber.log.Timber
import timber.log.Timber.i
import java.lang.reflect.Type
import java.util.*

// declare the filename
const val JSON_FILE = "tasks.json"
// declaring a utility to serialize a java class (pretty printing it)
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
// create an object to convert JSON string to a java collection
val listType: Type = object : TypeToken<ArrayList<DIYModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class DIYJSONStore(private val context: Context) : DIYStore {

    var tasks = mutableListOf<DIYModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<DIYModel> {
        logAll()
        return tasks
    }

    override fun create(task: DIYModel) {
        task.id = generateRandomId()
        tasks.add(task)
        serialize()
    }


    override fun update(task: DIYModel) {
        val tasksList = findAll() as ArrayList<DIYModel>
        var foundTask: DIYModel? = tasksList.find { t -> t.id == task.id }
        if (foundTask != null) {
            foundTask.title = task.title
            foundTask.description = task.description
            foundTask.rating = task.rating
            foundTask.image = task.image
        }
        serialize()
    }

    override fun delete(task: DIYModel) {
        val tasksList = findAll() as ArrayList<DIYModel>
        var foundTask: DIYModel? = tasksList.find { t -> t.id == task.id }
        if (foundTask != null) {
            tasks.remove(task)
            i("Task: ${tasks} removed")
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(tasks, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        tasks = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        tasks.forEach { Timber.i("$it") }
    }
}
// parse uri image property in DIYModel.
class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }
    // Utility to format a java class (pretty printing it)
    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}