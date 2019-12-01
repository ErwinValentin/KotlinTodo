package com.valentingonzalez.android.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.valentingonzalez.android.R
import com.valentingonzalez.android.activities.MainActivity
import java.util.*


class AlarmService : IntentService("AlarmService") {

    private lateinit var notification: Notification

    companion object {
        val CHANNEL_ID = "9001"
        val CHANNEL_NAME = "Reminder Channel"
        val CHANNEL_DESC = "Default Channel"
    }



    private fun createChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESC
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        createChannel()

        var time: Long = intent!!.getLongExtra("date",0)
        if(time > 0){
            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifyIntent = Intent(this, MainActivity:: class.java)
            val title = "Task is due today!"
            val description = intent.getStringExtra("description")

            notifyIntent.putExtra("title", title)
            notifyIntent.putExtra("description", description)
            notifyIntent.putExtra("notification", true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            val id = intent.getLongExtra("id",42).toInt()
            val pendingIntent = PendingIntent.getActivity(context,id,notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val res = this.resources
            //val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = Notification.Builder(this, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle().bigText(description))
                    .setContentText(description)
                    .build()
            }else{
                notification = Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle().bigText(description))
                    .setContentText(description)
                    .build()

            }
            notification.flags = Notification.FLAG_AUTO_CANCEL
            notificationManager.notify(id,notification)
        }
    }

}