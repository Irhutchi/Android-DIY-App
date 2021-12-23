package ie.wit.doityourself.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface DIYStore {
    fun findAll(taskList: MutableLiveData<List<DIYModel>>)
    fun findAll(userid: String, taskList: MutableLiveData<List<DIYModel>>)
    fun findById(userid: String, taskid: String, task: MutableLiveData<DIYModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, task: DIYModel)
    fun update(userid: String, taskid: String, task: DIYModel)
    fun delete(userid: String, taskid: String)
}


//interface DIYStore {
//    fun findAll(): List<DIYModel>
//    //    fun findAll(taskList: MutableLiveData<List<DIYModel>>)
//    fun findById(id: Long) : DIYModel?
//    fun create(task: DIYModel)
//    fun update(task: DIYModel)
//    fun delete(task: DIYModel)
//}