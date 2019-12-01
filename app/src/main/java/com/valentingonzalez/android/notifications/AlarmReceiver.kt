package com.valentingonzalez.android.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent){
        Log.d("RECEIVER","received")
        val service = Intent(context, AlarmService:: class.java)
        service.putExtra("date", intent.getLongExtra("date",0))
        service.putExtra("description", intent.getStringExtra("description"))
        service.putExtra("id", intent.getLongExtra("id",-1))

        context.startService(service)
    }
}