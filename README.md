# Assignment - DIY App.
Mobile Application Development - Assignment Two

A mobile application created using Android Studio and written in Kotlin. It forms part of a submission for assignment two of Mobile Application Development module at [WIT](https://www.wit.ie/courses/hdip-computer-science-parttime).  

---
## Features

- Full CRUD Functionality
- Model View ViewModel Design Pattern (MVVM) 
- App connected Google Firebase
   - Basic and Google Authentication
       - Password Reset 
   - Cloud Storage
   - Realtime Database
- Upload, Update profile image (Picasso Image Library)
- Swipe Support (refresh recycler view, edit & delete)
- Splash Screen
- Take Photos (CameraX API)

---
## Installation

To run this project yourself: <br>

*Note* : this project repo does not include the **google-services.json** file and isn't connected to Firebase. You will need to connect this app to your own Firebase Console App for it to build and run.

1. Clone this repository and import into Android Studio <br> 
   - `https://github.com/Irhutchi/DoItYourself.git` <br>
2. Go to AVD Manager an click `+ Create Virtual Device`
3. Select device (phone) add & choose appropiate SDK. See build.gradle file for SDK setup.
4. Click `Next`
5. With Android Emulator installed, click `run` button.

The emulator responds by installing and running the application.


---
## UX Design 
The objective is to allow the user to complete tasks and move between different activities with the least friction possible. 

**Authentication** <br>
Firebase Authentication does a lot of the heavy lifting in handling user registration and login. It provides back-end services, SDKs and ready made UI libraries. The firebase console is configured to authenticate users via basic auth and using their Google email account [1]. If a user forgets their password, they have the ability to reset via email verification link [11].  <br>

<img src="/app/src/images/google_login.png" width="200" height="325" /> <img src="/app/src/images/login_screen.png" width="200" height="325" /> <br>

**Navigation** <br>
In app navigation is handled using the Jetpack Navigation Component. It handles the navigation between fragments via the navigation drawer. 

<img src="/app/src/images/nav_drawer.png" width="200" height="325" /> <br>

The three main components of navigation [2]:

1. Nav Graph <br>
All the navigation information is visualised via the *nav_graph*. It is an XML file that contains all of the apps destinations along with actions (arrows) that the user can take to navigate from one destination to another. It resides in the res directory.
2. Nav Host <br>
The nav host fragment is setup in the file named *content_home.xml*. It is embedded into the UI layout and serves as the palceholder for the destinations. 
3. Nav Controller <br>
Each nav host fragment has an instance of nav controller. Using the navigation method of the navController class willl perform the navigation based on the destination id given in the nav_graph. Code snippet from ~ui/list/DiyListFragment.onCreateView

```
fragBinding.fab.setOnClickListener{
            val action = DiyListFragmentDirections.actionDiyListFragmentToDiyFragment()
            findNavController().navigate(action)
        }
```

<br><img src="/app/src/images/nav_graph.png" width="750" height="475" />
<br>

**Fragments** 
The use of fragment are used extensively throughout development. These are very useful as they are reusable and have their own lifecycles. When a user commits an action, for example creating a new task. The NavController handles the transaction by taking from the back stack allowing the user navigate backward to the list view actvity.[3]

**CameraX API** <br>
CameraX is a Jetpack support library [4]. It provides a way to interface with the devices camera. Before the app can access the camera, it will prompt the user to permission to access the camera. <br>
<img src="/app/src/images/camera_permissions.png" width="185" height="325" /> <img src="/app/src/images/camera_fragment.png" width="185" height="325" /> <br>

This function is implemented in the onCreate() method.

```
private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireActivity().baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
```

If permission is granted by the user the camera will start. The viewfinder is used to let the user preview the photo they will be taking. An instance of the `ProcessCameraProvider` is used to bind the lifecycle of cameras to the lifecycle owner. This eliminates the task of opening and closing the camera since CameraX is lifecycle-aware. <br>
*ImageCapture* use case - To capture photos, an image capture listener is triggered when the photo button is clicked. 
```
this@CameraFragment.activity?.let { ContextCompat.getMainExecutor(it) }?.let {
            imageCapture.takePicture(
                outputOption, it,
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                        val savedUri = Uri.fromFile(photoFile)
                        val msg = "Photo Saved"

                        Toast.makeText(this@CameraFragment.activity,
                            "$msg $savedUri",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    override fun onError(exception: ImageCaptureException) {
                        i("onError: ${exception.message}",)
                    }
                }
            )
        }
```
An file is created to store the image. Each image is timestamped which ensures each is unique. 
```
val photoFile = File(
            outputDirectory, SimpleDateFormat(Constants.FILE_NAME_FORMAT,
                Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg")
```
After the image is taken successfully, the photo is saved to the file created previously. 

<br>**Swipe Support**

- Swipe-to-Refresh: <br>
   Swipe-to-refresh user interface is implemented within the *SwipeRefreshLayout* widget. As the diy tasks are maintained remotely, it is essential to give the user access to the most up to date list . It detects a vertical swipe by a user, displays a progress bar and triggers a callback method. Depending on the user settings it will either update showing only the items the user created or an entire list by all users. When the refresh call has finished. <br>
   ```
   fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Diy List")
            if(!diyListViewModel.readOnly.value!!) {
                diyListViewModel.loadAll()
            } else
                diyListViewModel.load()
        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }
   ```
   
- Swipe-to-Delete <br>
   When a user swipes left it triggers a callback function *SwipeToDeleteCallback* located in the utils package. The swipe feature is handled in the DiyList fragment. Using a *tag* property of the view and tagging it with the task 'id' -  the 'id' is retrieved removes the diy task from both the recyclerView and the server.

   ```
   val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Task")
                val adapter = fragBinding.recyclerView.adapter as DIYAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                diyListViewModel.delete(diyListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as DIYModel).uid!!)
                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)
        ```

- Swipe-to-Edit <br>
   Similar to how the delete functions, when a user swipes right a callback to *SwipeToEditCallback* in the utils package. This callback navigates the user to the Diy Edit Fragment. 
   ```
   val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onDIYClick(viewHolder.itemView.tag as DIYModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)
   ```


**Splash Screen** <br>
The Splash screen is the first screen visible to the user when the application is launched. It contains an progress bar with a welcome message [5]. Their are issues with this current commit and splash screen does not run. It is a work in progress. 
<br><img src="/app/src/images/splash_screen.PNG" width="185" height="325" />
<br>
**Persistent Storage** <br>
Early iterations of this app utilised read/write to JSON file. Contents could be viewed via device file explorer inside Android Studio. <br>
<img src="/app/src/images/json.png" width="750" height="475" />
<br>
<br>
<br>
**Toolbar Menu** <br>
<img src="/app/src/images/nav_menu.png" width="185" height="325" />
<br>

---
## Developer Experience (DX)
The MVVM approach taken in this project is currently the industry-recognised arhictecture. It overcomes the drawbacks of the MVP and MVC design patterns [6]. From a developers point of view, it is easy to evaluate the structure as the business and presentaion logic (views/UI) is seperated from the user interface (view). Data is exposed in *DIYModel* to the ViewModel via LiveData (observables). The view only observes and is notified of any changes in state. 

Sharing data between fragments utilizes the  `byactivityViewModels()` method. It is particularly useful in support of multi-user application. It helps keep track of shared viewModels of across the entire scope of the entire application. This approcach increases the code maintainabity thus the DX. 

<br><img src="/app/src/images/mvvm.png" width="750" height="475"/><br>

## References

[1] [Firebase Quickstart](https://github.com/firebase/quickstart-android) <br>
[2] [Using Android Jetpack Navigation Component](https://medium.com/kayvan-kaseb/using-android-jetpack-navigation-component-a3ed8ce4c8e8) <br>
[3] [Explore Fragments](https://developer.android.com/codelabs/kotlin-android-training-create-and-add-fragment#0) <br>
[4] [Getting Started with CameraX](https://developer.android.com/codelabs/camerax-getting-started#0) <br>
[5] [How to Create a Splash Screen in Android using Kotlin](https://www.geeksforgeeks.org/how-to-create-a-splash-screen-in-android-using-kotlin/#:~:text=Android%20Splash%20Screen%20is%20the%20first%20screen%20visible,information%20about%20the%20company%20logo%2C%20company%20name%2C%20etc.) <br>
[6] [ViewModel magic revealed](https://proandroiddev.com/viewmodel-magic-revealed-330476b5ab27) <br>
[7] [YouTube Fragment Tutorial](https://youtu.be/6OlONE8Lb_4) <br>
[8] [Use of Radio Buttons](https://youtu.be/2LWKXXLuNrk) <br>
[9] [Kotlin docs](https://kotlinlang.org/docs/home.html) <br>
[10] [Implement Dark Mode In Android](https://www.section.io/engineering-education/how-to-implement-dark-mode-in-android-studio/)<br>
[11] [Reset Password - Firebase](https://www.youtube.com/watch?v=vjVyUKVNd3U&ab_channel=CodeAndroid)<br>


## Author
- [@irhutchi](https://github.com/Irhutchi)
