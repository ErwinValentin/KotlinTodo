package com.valentingonzalez.android.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*

class NotificationManagerService {

    fun sendNotification(id: Long,desc: String,time: Long, activity: Activity){
        if(time > 0){
            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity.applicationContext, AlarmReceiver::class.java)
            alarmIntent.putExtra("date", time)
            alarmIntent.putExtra("id", id)
            alarmIntent.putExtra("description",desc)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time

            val pendingIntent = PendingIntent.getBroadcast(activity, id.toInt(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
    fun cancelNotification(id: Long){
        //TODO cancel intent using the id, convert to int
    }
}