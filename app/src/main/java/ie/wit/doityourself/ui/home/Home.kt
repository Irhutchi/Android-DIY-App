package ie.wit.doityourself.ui.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseUser
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.HomeBinding
import ie.wit.doityourself.databinding.NavHeaderBinding
import ie.wit.doityourself.firebase.FirebaseImageManager
import ie.wit.doityourself.helpers.showImagePicker
import ie.wit.doityourself.ui.auth.LoggedInViewModel
import ie.wit.doityourself.ui.auth.Login
import ie.wit.doityourself.utils.readImageUri
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var headerView : View
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        initNavHeader()
        // ensure  nav drawer opens above our toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.diyFragment,
            R.id.diyEditFragment,
            R.id.diyListFragment,
            R.id.cameraFragment,
            R.id.aboutusFragment,
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_DoItYourself_NoActionBar) //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.Theme_DoItYourself)  //default app theme
        }


//        val toggle: ToggleButton = findViewById(R.id.toggleTheme)
////        val item = menu.findById(R.id.toggleTheme) as MenuItem
////        item.setActionView(R.layout.toggle_theme)
////        val toggleTheme: SwitchCompat = item.findViewById(R.id.toggleButton)
////        toggleTheme.isChecked = false
//
//        toggle.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                Toast.makeText(this, if(isChecked) "Dark Mode ON" else "Dark Mode OFF", Toast.LENGTH_SHORT).show()
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//
//        }

    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(firebaseUser)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

        registerImagePickerCallback()
    }

    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderImage.setOnClickListener {
            showImagePicker(intentLauncher)
            Toast.makeText(this,"Click To Change Image",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        FirebaseImageManager.imageUri.observe(this, { result ->
            if(result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.navHeaderImage,
                        false)
                }
                else
                {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.person_icon,
                        navHeaderBinding.navHeaderImage)
                }
            }
            else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.navHeaderImage, false)
            }
        })
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if(currentUser.displayName != null)
            navHeaderBinding.navHeaderName.text = currentUser.displayName
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    // onClick nav_drawer sign out func is called
    fun signOut(item: android.view.MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderBinding.navHeaderImage,
                                    true)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }


}
