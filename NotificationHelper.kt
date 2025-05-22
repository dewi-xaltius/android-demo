package com.example.pizza // IMPORTANT: Ensure this matches your app's package name

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.pizza.R // This is needed to access your R.drawable resources

// A unique ID for our notification channel
const val CHANNEL_ID = "pizza_order_channel"
const val NOTIFICATION_ID = 1 // A unique ID for the notification itself

fun sendOrderStatusNotification(context: Context) {
    // 1. Get the NotificationManager system service
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 2. Create a Notification Channel (required for Android 8.0 Oreo / API 26 and higher)
    // The channel defines the importance, sound, vibration, etc., for a category of notifications.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Check if API level is 26 (Oreo) or higher
        val channel = NotificationChannel(
            CHANNEL_ID, // The unique ID for this channel
            "Pizza Order Status", // User-visible name for the channel in system settings
            NotificationManager.IMPORTANCE_DEFAULT // How important these notifications are (e.g., sound, vibration)
        ).apply {
            description = "Notifications for pizza order delivery status updates" // User-visible description
        }
        notificationManager.createNotificationChannel(channel)
    }

    // 3. Build the Notification content
    // We use NotificationCompat.Builder for backward compatibility across Android versions
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.baseline_local_pizza_24) // You MUST have an icon named 'pizza_icon' in your res/drawable folder
        // This is the small icon that appears in the status bar.
        // It should generally be a white silhouette on a transparent background.
        .setContentTitle("Your Pizza Order") // The main title of the notification
        .setContentText("Your order is on its way to you!") // The main text of the notification
        .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set priority for older Android versions (pre-API 26)
        .setAutoCancel(true) // Dismisses the notification automatically when the user taps on it

    // 4. Send the Notification to the system
    notificationManager.notify(NOTIFICATION_ID, builder.build())
}
