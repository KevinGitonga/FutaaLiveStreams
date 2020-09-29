package ke.co.ipandasoft.futaasoccerlivestreams.ui.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.ui.splash.SplashActivity
import timber.log.Timber

class FirebaseNotificationService: FirebaseMessagingService() {

    private val TAG = "NOTIFICATION SERVICE"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Timber.e(TAG+"From: " + remoteMessage.from)
        Timber.e(TAG+"Notification Message Body: " + remoteMessage.notification!!.body)
        val title = remoteMessage.notification!!.title
        val label = remoteMessage.notification!!.body
        sendNotification(title!!, remoteMessage.notification!!.body!!)
    }


    private fun sendNotification(title:String, messageBody:String) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_soccer_livestreams)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            notificationBuilder.priority = android.app.Notification.PRIORITY_HIGH
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

}