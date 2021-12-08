package ie.wit.doityourself.main

/*
   A single instance of this class will be created when the application is launched.
   A reference to this application can be acquired in other activities as needed.
*/

import android.app.Application
import ie.wit.doityourself.models.DIYManager
import timber.log.Timber


class MainApp : Application() {

//    lateinit var diyStore: DIYStore
//    lateinit var tasks: DIYStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        diyStore = DIYManager()
//        diyStore = DIYMemStore()
//        tasks = DIYJSONStore(applicationContext)
        Timber.i("DIY App started")
    }
}
