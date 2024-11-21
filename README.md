# Notification Helper
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.1-blue)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

## Introduction to Notification Helper
Hey there, Android enthusiast! 🎉
Notifications are the heart and soul of engaging mobile apps.
Whether you're reminding users of a sale, nudging them to check their inbox, or pinging them about an epic event, you need a smart way to manage notifications.
Enter Notification Helper, your all-in-one utility class for taking the stress out of managing Android notifications.

With Notification Helper, you focus on crafting impactful notifications, and it handles the nitty-gritty stuff behind the scenes.
Ready to boost your app’s user experience?
Let’s dive into the fun! 🚀




## What Problem Does the Notification Helper Solve?
Managing notifications on Android has evolved over time, and while it's more powerful, it’s also gotten a bit more complicated. Here’s a quick rundown:
* **Before Android Oreo (API 26):** Notifications were easy—just turn them on or off. Simple as that! ✅
* **With Android Oreo (API 26):** Notification channels were introduced, allowing developers to organize notifications into categories. Now, users can control how they receive different types of notifications—think of it like setting up your own notification playlist!
* **With Android 13 (Tiramisu, API 33):** Runtime notification permissions made their debut, meaning apps now need explicit permission from users to send notifications. It’s like asking for permission before sending a message. 📩

With all these changes across different Android versions, managing notifications can feel like juggling multiple tasks at once. 😅🤹‍♀️

But fear not—Notification Helper is here to save the day! 🦸‍♂️ This library handles everything for you, from managing channels to asking for permission, so you don’t have to stress. Let it take care of the complicated stuff, and you can focus on building your amazing app! 🚀🎉




## 🚀 Features That Will Make You Jump for Joy!
### Check If Notifications Are Enabled ✔️
Wondering if notifications are working?
No problem! Notification Helper will check for you and let you know if notifications are enabled or if the required permissions are in place. 🕵️‍♀️🔔

### Create Notification Channels in a Snap 🎨
No more fussing around with complex code.
With Notification Helper, creating notification channels is super easy.
Just give it an ID, name, description, and importance level, and voilà!
Your channel is ready to go—no stress. 📢

### See Which Channels Are Disabled or Enabled 🔒
With Notification Helper, you can easily see which notification channels are turned off (or on!) based on their importance levels.
That way, you’ll always know if your app is sending notifications or not. 📩

### Handle Notification Across All Android Versions 🛂
Whether you’re on Android Tiramisu (API 33), Oreo (API 26), or anything older, Notification Helper ensures you get the right permissions when you need them. On Android Tiramisu (API 33), it even requests runtime notification permissions using the latest permission requests! ✅
For older versions, I’ll take care of navigating users to the right settings screen so they can easily adjust notification preferences. 🍰

### Take Users to the Right Settings Screen 📍
Android is picky about where you can change notification settings, but Notification Helper knows exactly where to send your users.
Whether it’s to the app’s notification settings, the general app settings, or earlier versions where such settings don't exist—I'm your GPS. 🗺️

### Perfect for Different Android Versions 🪄
Whether you're rocking an old-school Android version (like Marshmallow) or a shiny new version, Notification Helper handles everything smoothly.
On versions prior to Android Oreo, there are no channels, but I'll make sure your app's notifications still work as expected—without the extra hassle! 📱✨




## 📦 How to Implement BerkBerber NotificationHelper in Your Project 🎉
Alright, you’ve decided to level up your Android app with NotificationHelper – great choice!
Now, let's dive into how to get this library running smoothly in your project.
It's as easy as 1-2-3 (or maybe 4, depending on your setup). Here's how you do it:

### 🚀 Quickstart for the build.gradle File
If you're using standard dependencies in your build.gradle file, it's pretty straightforward.
1. Just add it to your dependencies:
```groovy
dependencies {
    implementation("com.github.berk-berber:notification-helper:1.0.1")
}
```
That's it! Your app will be rocking the notifications in no time.

### 🔧 Using libs.versions.toml for Dependency Management
Do you love clean, manageable dependency versions (who doesn't, right?)?
If you're using libs.versions.toml to manage your dependencies, here’s how you can implement NotificationHelper without any sweat:
#### Step 1: Add the version to your `libs.versions.toml` file
First, let’s add the version number of NotificationHelper to the versions section
```toml
berkberberNotificationHelper = "1.0.1"
```
#### Step 2: Define the library in the libraries section
Now, let’s make it available by adding it to the libraries section:
```toml
berkberber-notificationhelper= { group = "com.github.berk-berber", name = "notification-helper", version.ref = "berkberberNotificationHelper" }
```

#### Step 3: Add the dependency in build.gradle
Finally, go to your `build.gradle` and just implement it like this:
```groovy
dependencies {
    implementation(libs.berkberber.notificationhelper)
}
```
Boom! Just like that, you’re ready to use NotificationHelper in your project.
No more copy-pasting versions all over the place!

### 🎉 You’re Good to Go!
With these simple steps, you'll have the NotificationHelper up and running, and you'll be boosting your app's notifications like a pro! 🏆

Happy coding, and may your app notifications be ever engaging! 📲




## How to Use Notification Helper
Using Notification Helper is a piece of cake 🍰. Let’s break it down step by step:

### Create an Instance
Activity? Super easy, just pass in the context:
```kotlin
 val notificationHelper = NotificationHelper(context = this)
```
Fragment? No worries, use requireContext():
```kotlin
val notificationHelper = NotificationHelper(context = requireContext())
```
Jetpack Compose? Just grab the context with LocalContext.current:
```kotlin
val context = LocalContext.current
val notificationHelper = NotificationHelper(context = context)
```

### Check if Notifications Are Enabled
Want to know if the user has enabled or disabled notifications? Simple!
```kotlin
val isEnabled = notificationHelper.isNotificationEnabled()
```

### Get the Enabled Notification Channels
Need a list of all the channels that are enabled? We’ve got you covered:
```kotlin
val enabledNotificationChannels = notificationHelper.getEnabledNotificationChannels()
```

### Get the Disabled Notification Channels
Want to see the channels that are disabled? Here's how:
```kotlin
val disabledNotificationChannels = notificationHelper.getDisabledNotificationChannels()
```

### Create Notification Channels
Setting up your notification channels? It’s a breeze. You can configure the ID, Name, Description, and Importance Level like so:
```kotlin
notificationHelper.createNotificationChannel(
    channelId = "your_channel_id",
    channelName = "your_channel_name",
    channelDescription = "your_channel_description",
    importance = NotificationChannelImportance.MAX
)
```
Want to customize the importance? Here are the levels you can choose from:
```kotlin
@RequiresApi(Build.VERSION_CODES.O)
enum class NotificationChannelImportance(val value: Int) {
    NONE(NotificationManager.IMPORTANCE_NONE),
    MIN(NotificationManager.IMPORTANCE_MIN),
    LOW(NotificationManager.IMPORTANCE_LOW),
    DEFAULT(NotificationManager.IMPORTANCE_DEFAULT),
    HIGH(NotificationManager.IMPORTANCE_HIGH),
    MAX(NotificationManager.IMPORTANCE_MAX)
}
```

### Enable Notifications Based on Android OS
No need to worry about OS-specific quirks. Notification Helper takes care of everything. Just call:
```kotlin
notificationHelper.enableNotification(notificationLauncher = notificationLauncher)
```
Now, to set up your notificationLauncher, here’s what you do:
#### In your Activity:
```kotlin
class YourActivity : ComponentActivity() {
    private val notificationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle the result of the permission request
    }
}
```
#### In your Fragment:
```kotlin
class YourFragment : Fragment() {
    private val notificationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle the result of the permission request
    }
}
```
#### In Compose (Best Practice):
Create the launcher in your Activity and pass it down to the Compose screen:
```kotlin
class YourComposeActivity : ComponentActivity() {

    private val notificationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle the result of the permission request
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestComposeScreen(notificationLauncher = notificationLauncher)
        }
    }
}
```
```kotlin
@Composable
fun YourComposeScreen(notificationLauncher: ActivityResultLauncher<String>) {
    val context = LocalContext.current
    Button(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        onClick = {
            NotificationHelper(context = context).enableNotification(notificationLauncher = notificationLauncher)
        }
    ) {
        Text(text = stringResource(R.string.enable_notification)) // Customize the button text
    }
}
```
And that’s it! 🎉 Just follow these simple steps, and you’re ready to roll with Notification Helper—handling all your notification management needs with zero hassle. 🦸‍♀️💥

## Don’t Worry, We Got You! 😎
Seriously, Notification Helper is here to make your life easier. All the code is well-documented so you don't have to guess what each function does. 📝💡

You can hover over any function to get detailed info about what it does, how it works, and what parameters you need to pass. So no need to stress! 😌✨

Everything’s ready for you to use, and if you want to dive deeper into any function or feature, just move your cursor over it and boom—instant documentation at your fingertips! 🚀🔍

So sit back, relax, and let the library do the heavy lifting while you focus on building something awesome! 💻🎉



## Compatibility 🎉

No worries, **Notification Helper** is here to play nicely with a wide range of Android versions! Here's the lowdown:

* **Minimum SDK:** Android 6.0 (API 23) – Yup, we're still rocking the classics! 🚀
* **Target SDK:** Android 15 (API 35) – Always aiming high for the latest and greatest! 📱✨

So whether you're holding onto that trusty Android 6.0 or showing off the newest Android 15, **Notification Helper** has got your back!




## Contributing
I’d absolutely love for you to get involved! 🤩
Whether you're squashing bugs, adding new features, or up the docs, every little bit makes Notification HelperHelper even better!

Here’s how you can jump in:

### Fork it! 🍴
Make your own copy of the repo and start playing around.
No need to ask—just fork it, and you’re off to the races!

### Branch out! 🌳
Create a new branch for whatever you’re working on.
This keeps things nice and clean.
Give it a fun name like feature-cool-notification or bugfix-still-not-snoozing.

### Write code like a wizard! ✨
Fix that bug, add a feature, or tidy up the code.
Keep things smooth and organized, because nobody likes a messy commit.

### Test, test, test! 🧪
Make sure everything still works perfectly after your changes.
Run all the tests and, if needed, add your own.
I love a bug-free zone!

### Push it and create a pull request! 🚀
Once you’re happy with your changes, push your branch and open a pull request (PR).
Be sure to write a friendly description so I know what you’ve done, and we can chat about it.

### Review and Merge 🔄
I’ll take a look at your PR, review it, and merge it if everything looks good.
And just like that, you’re an official contributor! 🎉

### A Few Ground Rules (Just to Keep Things Neat):
#### Be respectful
Let’s keep it kind and welcoming—every contribution matters!

#### Keep it simple
Write clean, readable code so I can understand it quickly. The easier it is, the faster I can help you.

#### Tests are your best friend
If you’re adding or fixing something, be sure to test it.
I want to make sure everything keeps running smoothly.

That’s it! Whether you're fixing a typo or adding something cool, it all helps. I can’t wait to see what you bring to the table. So, what are you waiting for? Let’s make something amazing together! 🙌✨
