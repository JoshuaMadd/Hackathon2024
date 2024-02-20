package be.vives.vivesplus.services

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import be.vives.vivesplus.MainActivity
import be.vives.vivesplus.util.PreferencesManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("---------------notificatie ontvangen-------------------")
        remoteMessage.notification?.let {
            val title = it.title
            val message = it.body
            val fragmentId = "test"

            // Show the notification to the user.
            showNotification(title, message, fragmentId)
        }
    }

    private fun showNotification(title: String?, message: String?, fragmentId: String) {
        val channelId = "0"

        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("fragmentid", fragmentId)

        // Create a pending intent that launches a new task and navigates to the desired activity
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)
            .setNumber(1)
            .setVibrate(longArrayOf(0, 1000, 1000, 1000))
            .setAutoCancel(true)

        if(areNotificationsEnabled){
            notificationManager.notify(1, builder.build())
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "token $token")
        PreferencesManager().writeStringToPreferences(this, "deviceToken", token)
    }
}