import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import uk.ac.tees.mad.d3424757.xpenseapp.R

// Notification ID
const val NOTIFICATION_ID = 101
const val CHANNEL_ID = "budget_alert_channel"

// Create a notification channel for Android 8.0 and above
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Budget Alert Channel"
        val descriptionText = "Notifications for budget alerts"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

// Trigger notification when the budget threshold is exceeded
fun triggerNotification(context: Context, budgetAmount: Double, threshold: Int) {
    val notificationManager = NotificationManagerCompat.from(context)
    val notification = buildNotification(context)

    // Check if budget exceeds threshold
    if (budgetAmount >= threshold) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(NOTIFICATION_ID, notification)
    } else {
        notificationManager.cancel(NOTIFICATION_ID)  // Remove notification if under threshold
    }
}

fun buildNotification(context: Context): Notification {
    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Budget Alert")
        .setContentText("Your budget has reached the set threshold!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        .setGroup("budget_alerts")
        .build()
}
