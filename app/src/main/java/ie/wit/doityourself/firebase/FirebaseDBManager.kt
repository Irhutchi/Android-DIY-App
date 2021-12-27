package ie.wit.doityourself.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.doityourself.models.DIYModel
import ie.wit.doityourself.models.DIYStore
import timber.log.Timber

object FirebaseDBManager: DIYStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(taskList: MutableLiveData<List<DIYModel>>) {
        database.child("tasks")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<DIYModel>()
                    val children = snapshot.children
                    children.forEach {
                        val task = it.getValue(DIYModel::class.java)
                        localList.add(task!!)
                    }
                    database.child("tasks")
                        .removeEventListener(this)

                    taskList.value = localList
                }
            })
    }

    override fun findAll(userid: String, taskList: MutableLiveData<List<DIYModel>>) {
        database.child("user-tasks").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<DIYModel>()
                    val children = snapshot.children
                    children.forEach {
                        val task = it.getValue(DIYModel::class.java)
                        localList.add(task!!)
                    }
                    database.child("user-tasks").child(userid)
                        .removeEventListener(this)

                    taskList.value = localList
                }
            })
    }

    override fun findById(userid: String, taskid: String, task: MutableLiveData<DIYModel>) {
        database.child("user-tasks").child(userid)
            .child(taskid).get().addOnSuccessListener {
                task.value = it.getValue(DIYModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, task: DIYModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("tasks").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        task.uid = key
        val diyValues = task.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/tasks/$key"] = diyValues
        childAdd["/user-tasks/$uid/$key"] = diyValues

        database.updateChildren(childAdd)
    }

    // func removes task from task collection and user-tasks' collection
    override fun delete(userid: String, taskid: String) {
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/tasks/$taskid"] = null
        childDelete["/user-tasks/$userid/$taskid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, taskid: String, task: DIYModel) {
        val diyValues = task.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["tasks/$taskid"] = diyValues
        childUpdate["user-tasks/$userid/$taskid"] = diyValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userDonations = database.child("user-tasks").child(userid)
        val allDonations = database.child("tasks")

        userDonations.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all donations that match 'it'
                        val donation = it.getValue(DIYModel::class.java)
                        allDonations.child(donation!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }

}