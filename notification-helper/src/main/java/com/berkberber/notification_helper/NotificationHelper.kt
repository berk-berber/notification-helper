package com.berkberber.notification_helper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * A helper class to manage notification-related functionality in an Android application.
 *
 * This class provides utilities for creating notification channels, checking notification permissions,
 * navigating to system settings, and interacting with notification channels programmatically.
 *
 * @param context The context used for accessing system services and launching intents.
 */
class NotificationHelper(private val context: Context) {

    private val intentDataPackage = "package"

    /**
     * Creates a new notification channel for the application.
     *
     * A notification channel is used to group and manage notifications based on their priority and behavior.
     * The channel's importance level determines how notifications in that channel are displayed (e.g., sound, vibration, etc.).
     * This method is only applicable for devices running Android O (API level 26) or higher.
     * If the device is running an earlier version of Android, the method does nothing.
     *
     * @param channelId The unique identifier for the notification channel. It should be unique within your app.
     * @param channelName The name of the channel, which is displayed to users in the notification settings.
     * @param channelDescription A brief description of the channel's purpose, which helps users understand its use.
     * @param importance The importance level of the channel, which determines how intrusive notifications in this channel will be.
     *                  This should be one of the values from [NotificationChannelImportance].
     *
     * @see NotificationChannel
     * @see NotificationChannelImportance
     */
    fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: NotificationChannelImportance
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(channelId, channelName, importance.value).apply {
            description = channelDescription
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Enables notifications for the application by navigating to the appropriate settings screen or requesting the necessary permissions, depending on the Android version.
     *
     * This method ensures that notifications are enabled for the app, adapting to different Android versions:
     * - On Android 13 (API level 33) or higher, it requests the runtime permission for posting notifications (`Manifest.permission.POST_NOTIFICATIONS`).
     * - On Android O (API level 26) through Android 12 (API level 31), it opens the app's notification settings screen, allowing the user to enable/disable notifications.
     * - On Android versions prior to Android O (below API level 26), it navigates to the app's general settings screen.
     *
     * @param notificationLauncher An [ActivityResultLauncher] that is used to request the runtime notification permission on Android 13+.
     *                             This is a launcher object for handling permission requests in a clean, lifecycle-aware manner.
     *
     * @see requestRuntimePermission
     * @see navigateToAppNotificationSettings
     * @see navigateToAppSettings
     */
    fun enableNotification(notificationLauncher: ActivityResultLauncher<String>) = doByOsVersion(
        doAfterTiramisu = { requestRuntimePermission(notificationLauncher = notificationLauncher,) },
        doAfterOreo = { navigateToAppNotificationSettings() },
        doBeforeOreo = { navigateToAppSettings() }
    )

    /**
     * Requests the runtime permission for posting notifications on Android 13 (API level 33) and above.
     *
     * This method is only available on devices running Android 13 or higher (Tiramisu). It launches an [ActivityResultLauncher]
     * to request the `POST_NOTIFICATIONS` permission, which is required by apps to send notifications to the user.
     * On Android versions prior to Android 13, this permission is not required, and the method will not be called.
     *
     * @param notificationLauncher An [ActivityResultLauncher<String>] used to handle the permission request in a lifecycle-aware manner.
     *                             This launcher is used to request permissions and handle the result of the request.
     *                             In this case, it launches a request for `Manifest.permission.POST_NOTIFICATIONS`.
     *
     * @see Manifest.permission.POST_NOTIFICATIONS
     * @see ActivityResultLauncher
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestRuntimePermission(notificationLauncher: ActivityResultLauncher<String>) {
        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    /**
     * Navigates the user to the app's notification settings screen.
     *
     * This method is available only on devices running Android O (API level 26) or higher.
     * It opens the settings screen where users can modify the notification preferences for the app, such as enabling or disabling notifications,
     * adjusting sound and vibration settings, and more.
     *
     * @see Settings.ACTION_APP_NOTIFICATION_SETTINGS
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToAppNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }

    /**
     * Navigates the user to the app's general settings screen.
     *
     * This method opens the application details settings screen, where the user can manage the app's permissions,
     * storage usage, battery optimization, and other system-level settings. This method works for devices running
     * any version of Android (since it uses a more general action) and is especially useful for older Android versions
     * (below Android O), where app-specific notification settings are not available.
     *
     * @see Settings.ACTION_APPLICATION_DETAILS_SETTINGS
     */
    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(intentDataPackage, context.packageName, null)
        }
        context.startActivity(intent)
    }

    /**
     * Checks if notifications are enabled for the application, based on the Android version.
     *
     * This method checks whether notifications are allowed for the app. The behavior varies depending on the Android version:
     * - On Android 13 (API level 33) and higher, it checks if the runtime permission to post notifications (`POST_NOTIFICATIONS`) is granted.
     * - On Android O (API level 26) through Android 12 (API level 31), it checks if the app’s notification settings are enabled.
     * - For Android versions before Android O, it assumes that notifications are enabled and returns `true`.
     *
     * @return `true` if notifications are enabled for the app, otherwise `false`.
     *
     * @see isRuntimeNotificationsPermissionGranted
     * @see isNotificationSettingEnabled
     */
    fun isNotificationEnabled() = doByOsVersion(
        doAfterTiramisu = { isRuntimeNotificationsPermissionGranted() },
        doAfterOreo = { isNotificationSettingEnabled() },
        doBeforeOreo = { isNotificationSettingEnabled() }
    )

    /**
     * Retrieves a list of notification channels that are currently disabled (i.e., with no importance) for the application.
     *
     * This method filters the app's notification channels to return those with an importance level of `IMPORTANCE_NONE`,
     * indicating that notifications for those channels are disabled.
     *
     * The behavior differs based on the Android version:
     * - On Android O (API level 26) and higher, it fetches all notification channels and filters out those with no importance.
     * - On versions prior to Android O, it returns an empty list, as notification channels do not exist on these versions.
     *
     * @return A list of disabled [NotificationChannel] objects for the app if the device is running Android O or higher.
     *         Returns an empty list if the device is running a version lower than Android O.
     *
     * @see NotificationChannel
     * @see NotificationManager.IMPORTANCE_NONE
     */
    fun getDisabledNotificationChannels() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getAllNotificationChannels().filter { channel ->
            channel.importance == NotificationManager.IMPORTANCE_NONE
        }
    } else {
        emptyList()
    }

    /**
     * Retrieves a list of notification channels that are currently enabled (i.e., with a non-zero importance level) for the application.
     *
     * This method filters the app's notification channels to return those with an importance level that is not `IMPORTANCE_NONE`,
     * indicating that notifications for those channels are enabled and actively shown to the user.
     *
     * The behavior differs based on the Android version:
     * - On Android O (API level 26) and higher, it fetches all notification channels and filters out those with `IMPORTANCE_NONE`.
     * - On versions prior to Android O, it returns an empty list, as notification channels do not exist on these versions.
     *
     * @return A list of enabled [NotificationChannel] objects for the app if the device is running Android O or higher.
     *         Returns an empty list if the device is running a version lower than Android O.
     *
     * @see NotificationChannel
     * @see NotificationManager.IMPORTANCE_NONE
     */
    fun getEnabledNotificationChannels() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getAllNotificationChannels().filter { channel ->
            channel.importance != NotificationManager.IMPORTANCE_NONE
        }
    } else {
        emptyList()
    }

    /**
     * Retrieves all notification channels associated with the application.
     *
     * This method fetches all the notification channels available for the app. The behavior differs based on the Android version:
     * - On Android O (API level 26) and higher, it retrieves the list of all notification channels.
     * - On Android versions prior to Android O, it returns an empty list, as notification channels were not introduced until Android O.
     *
     * @return A list of [NotificationChannel] objects if the device is running Android O or higher.
     *         Returns an empty list if the device is running a version lower than Android O.
     *
     * @see NotificationChannel
     * @see NotificationManagerCompat
     */
    private fun getAllNotificationChannels() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getNotificationCompatManager().notificationChannels
    } else {
        emptyList<NotificationChannel>()
    }

    /**
     * Checks if the runtime permission to post notifications is granted on Android 13 (API level 33) or higher.
     *
     * This method checks whether the app has been granted the `POST_NOTIFICATIONS` permission, which is required
     * starting from Android 13 (API level 33) to allow the app to send notifications. The permission is considered
     * granted if the result of the permission check is `PackageManager.PERMISSION_GRANTED`.
     *
     * This method is only applicable to devices running Android 13 (API level 33) or higher, as the `POST_NOTIFICATIONS`
     * permission was introduced in that version. For devices running earlier versions, this check is not needed.
     *
     * @return `true` if the app has been granted the `POST_NOTIFICATIONS` permission; `false` otherwise.
     *
     * @see Manifest.permission.POST_NOTIFICATIONS
     * @see PackageManager.PERMISSION_GRANTED
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isRuntimeNotificationsPermissionGranted(): Boolean = ContextCompat
        .checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    /**
     * Checks if all notification channels are enabled (i.e., have non-zero importance).
     *
     * This method checks whether all notification channels in the app are enabled by verifying that none of the channels
     * have an importance level of `IMPORTANCE_NONE`. If any channel has `IMPORTANCE_NONE`, which means it is disabled,
     * the function returns `false`.
     *
     * The behavior differs based on the Android version:
     * - On Android O (API level 26) and higher, it checks each notification channel and verifies that the importance is
     *   not `IMPORTANCE_NONE`.
     * - On Android versions prior to Android O, notification channels do not exist, and the method assumes that all channels
     *   are enabled, returning `true`.
     *
     * @return `true` if all notification channels are enabled (i.e., have an importance level greater than `IMPORTANCE_NONE`).
     *         Returns `false` if any channel is disabled (i.e., has `IMPORTANCE_NONE`).
     *
     * @see NotificationManager.IMPORTANCE_NONE
     * @see getAllNotificationChannels
     */
    private fun areAllNotificationChannelsEnabled(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAllNotificationChannels().forEach { notificationChannel ->
                if (notificationChannel.importance == NotificationManager.IMPORTANCE_NONE) {
                    return false
                }
            }
            return true
        } else {
            return true
        }
    }

    /**
     * Checks if notifications are enabled for the application.
     *
     * This method uses the `NotificationManagerCompat` to check whether notifications are enabled for the app.
     * It determines if the app is allowed to post notifications, regardless of individual notification channel settings.
     *
     * On Android versions that support notification channels (Android O and later), this method checks if the app
     * is allowed to post notifications overall. This check does not account for individual channel settings.
     *
     * @return `true` if notifications are enabled for the app, and `false` if notifications are disabled.
     *
     * @see NotificationManagerCompat.areNotificationsEnabled
     */
    private fun isNotificationSettingEnabled() = getNotificationCompatManager().areNotificationsEnabled()

    /**
     * Retrieves the [NotificationManagerCompat] instance for managing notifications.
     *
     * This method returns a `NotificationManagerCompat` instance, which is used to interact with the notification
     * system in a backward-compatible way. It provides methods to manage notifications, including checking if
     * notifications are enabled and posting new notifications.
     *
     * The `NotificationManagerCompat` class ensures compatibility across different Android versions, allowing
     * apps to manage notifications on devices running Android versions from Jelly Bean (API level 16) and above.
     *
     * @return The [NotificationManagerCompat] instance for the app.
     *
     * @see NotificationManagerCompat
     */
    private fun getNotificationCompatManager(): NotificationManagerCompat = NotificationManagerCompat.from(context)

    /**
     * Executes different actions based on the current Android OS version.
     *
     * This method checks the device's Android OS version and executes the appropriate action (provided as a lambda)
     * based on whether the version is at or above Android 13 (Tiramisu), Android 8 (Oreo), or lower.
     *
     * - If the OS version is **Android 13 (Tiramisu)** or higher, the `doAfterTiramisu` action is executed.
     * - If the OS version is **Android 8 (Oreo)** or higher but lower than Android 13, the `doAfterOreo` action is executed.
     * - If the OS version is **below Android 8 (Oreo)**, the `doBeforeOreo` action is executed.
     *
     * This method helps handle conditional behavior based on the OS version, ensuring compatibility with different Android versions.
     *
     * @param doAfterTiramisu A lambda function to execute if the device is running **Android 13 (Tiramisu)** or higher.
     * @param doAfterOreo A lambda function to execute if the device is running **Android 8 (Oreo)** or higher, but lower than Android 13.
     * @param doBeforeOreo A lambda function to execute if the device is running an Android version **lower than Android 8 (Oreo)**.
     *
     * @return The result of the executed lambda function (type `T`).
     */
    private fun <T> doByOsVersion(
        doAfterTiramisu: () -> T,
        doAfterOreo: () -> T,
        doBeforeOreo: () -> T
    ) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            doAfterTiramisu()
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            doAfterOreo()
        }

        else -> {
            doBeforeOreo()
        }
    }
}
