package com.valentingonzalez.android.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.valentingonzalez.android.R
import com.valentingonzalez.android.activities.MainActivity
import com.valentingonzalez.android.database.TaskClass
import com.valentingonzalez.android.database.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WidgetListService : RemoteViewsService(){
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return WidgetListRemoteViewsFactory(this.applicationContext, intent!!)
    }

}

class WidgetListRemoteViewsFactory(
    private val context: Context,
    intent: Intent
):RemoteViewsService.RemoteViewsFactory{
    val EXTRA_ITEM = "com.valentingonzalez.android.widget.EXTRA_ITEM"
    val WIDGET_ID = "WIDGET_ID"
    private var items= ArrayList<TaskClass>();
    private var count = 0;
    private var widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    var viewModelJob = Job()
    val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)



    override fun onCreate() {
        this.getList()
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_loading_view)
    }

    override fun getItemId(p0: Int): Long {
        return items[p0].id!!
    }

    override fun onDataSetChanged() {
        readAllTasks()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(p0: Int): RemoteViews {
        var item = items[p0]
        //Log.d("widget",item.toString())

        var intent = Intent(context, MainActivity::class.java)
        //var pendingIntent = PendingIntent.getActivity(context,0,intent,0)


        var view = RemoteViews(context.packageName, R.layout.widget_list_item)
        view.setTextViewText(R.id.widget_desc, item.name)
        view.setTextViewText(R.id.widget_date, item.date)

        //view.setOnClickPendingIntent(R.id.widget_checkbox, pendingIntent)

        val b = Bundle()
        b.putLong(EXTRA_ITEM, item.id!!)
        b.putInt(WIDGET_ID, widgetId)
        val fillIntent = Intent()
        fillIntent.putExtras(b)
        view.setOnClickFillInIntent(R.id.widget_checkbox, fillIntent)

        return view
    }


    override fun getCount(): Int {
        return count

    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
       // items.clear()
    }

    fun getList(){
        ioScope.launch {
            readAllTasks()
        }
    }

    fun readAllTasks(){
        items.clear()
        items.addAll(TodoDatabase.getInstance(context)!!.tasksDAO().getNotCompletedAsList())
        count = items.size
    }

}
