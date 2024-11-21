package com.berkberber.notification_helper

import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Represents the levels of importance for a notification channel.
 *
 * This enum maps directly to the importance levels defined in the
 * [NotificationManager] class. Importance levels determine how notifications
 * are presented to the user and how much attention they demand.
 *
 * Note: This enum requires API level 26 (Android O) or higher.
 *
 * @property value The integer value corresponding to the notification importance level.
 */
@RequiresApi(Build.VERSION_CODES.O)
enum class NotificationChannelImportance(val value: Int) {

    /**
     * No importance. Notifications will not be shown to the user.
     * @see NotificationManager.IMPORTANCE_NONE
     */
    NONE(NotificationManager.IMPORTANCE_NONE),

    /**
     * Minimum importance. Notifications are only shown in the system tray, without sound or visual interruption.
     * @see NotificationManager.IMPORTANCE_MIN
     */
    MIN(NotificationManager.IMPORTANCE_MIN),

    /**
     * Low importance. Notifications are shown in the system tray and may produce a status bar icon,
     * but do not interrupt the user.
     * @see NotificationManager.IMPORTANCE_LOW
     */
    LOW(NotificationManager.IMPORTANCE_LOW),

    /**
     * Default importance. Notifications make sound and appear as a heads-up notification if applicable.
     * @see NotificationManager.IMPORTANCE_DEFAULT
     */
    DEFAULT(NotificationManager.IMPORTANCE_DEFAULT),

    /**
     * High importance. Notifications make sound, appear prominently, and may interrupt the user.
     * @see NotificationManager.IMPORTANCE_HIGH
     */
    HIGH(NotificationManager.IMPORTANCE_HIGH),

    /**
     * Maximum importance. Reserved for critical notifications that require the user's immediate attention.
     * @see NotificationManager.IMPORTANCE_MAX
     */
    MAX(NotificationManager.IMPORTANCE_MAX)
}
