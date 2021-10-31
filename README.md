# Assignment - DIY App.
Mobile Application Development - Assignment One

A mobile application create using Android Studio aimed at DIY enthusiasts. 
The [Android](https://developer.android.com/kotlin) app that allows users to
create, view, update and delete tasks. The tasks are persisted using a file helper. 
We can read/write to JSON file and view contents via device file explorer inside Android Studio.
Tasks are listed in a recycler view.

## Features

- Create, Read, Update, Delete
- Upload images (Picasso Image Library)
- DIY tasks stored locally in JSON
- Slash Screen
- Take Photos (CameraX API)
- Adherence to Android Best Practices

## Installation

To run this project yourself: <br>
1. Clone this repository and import into Android Studio <br> 
   - `https://github.com/Irhutchi/DoItYourself.git`
2. Go AVD Manager an click `+ Create Virtual Device`
3. Select device (phone) add choose appropiate SDK. See build.gradle file for SDK setup.
4. Click `Next`
5. With Android Emulator installed, click `run` button.

The emulator responds by installing and running the application.


## UI Design - Sample Views

**Splash Screen** <br>
<img src="/app/src/images/splash_screen.PNG" width="200" height="325" />
<br>
**Persistent Storage** <br>
<img src="/app/src/images/json.png" width="750" height="475" />
<br>
<br>
**Navigation Drawer** <br>
<img src="/app/src/images/nav_drawer.png" width="200" height="325" />
<br>
<br>
**Toolbar Menu** <br>
<img src="/app/src/images/nav_menu.png" width="200" height="325" />
<br>
<br>
**Camera Permission** <br>
<img src="/app/src/images/camera_permissions.png" width="200" height="325" />
<br>
<br>
**Camera Fragment** <br>
<img src="/app/src/images/camera_fragment.png" width="200" height="325" />
<br>


## References

[1] [Getting Started with CameraX](https://developer.android.com/codelabs/camerax-getting-started#0) <br>
[2] [YouTube Fragment Tutorial](https://youtu.be/6OlONE8Lb_4) <br>
[3] [Use of Radio Buttons](https://youtu.be/2LWKXXLuNrk) <br>
[4] [Kotlin docs](https://kotlinlang.org/docs/home.html) <br>

## Author
- [@irhutchi](https://github.com/Irhutchi)
