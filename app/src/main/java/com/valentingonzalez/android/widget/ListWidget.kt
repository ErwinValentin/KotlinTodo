package com.valentingonzalez.android.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.valentingonzalez.android.R
import com.valentingonzalez.android.database.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * Implementation of App Widget functionality.
 */
class ListWidget : AppWidgetProvider() {

    val WIDGET_CLICKED_ACTION = "com.valentingonzalez.android.widget.WIDGET_CLICKED_ACTION"
    val EXTRA_ITEM = "com.valentingonzalez.android.widget.EXTRA_ITEM"
    var viewModelJob = Job()
    val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetId, appWidgetManager)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetId: Int,
        appWidgetManager: AppWidgetManager
    ) {
        val intent = Intent(context, WidgetListService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }

        val views = RemoteViews(context.packageName, R.layout.widget_list_layout).apply {
            setRemoteAdapter(R.id.widget_list, intent)

            setEmptyView(R.id.widget_list, R.id.appwidget_text)
        }

        //views.setTextViewText(R.id.appwidget_text, widgetText)
        val toastIntent = Intent(context, ListWidget::class.java)
        toastIntent.action = WIDGET_CLICKED_ACTION
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        val toastPendingIntent = PendingIntent.getBroadcast(
            context, 0, toastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setPendingIntentTemplate(R.id.widget_list, toastPendingIntent)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
        updateWidgetInfo(context,appWidgetId)
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals(WIDGET_CLICKED_ACTION)) {
            val taskIndex = intent.getLongExtra(EXTRA_ITEM, -1)
            val widgetId = intent.getIntExtra("WIDGET_ID", -1)
            startDeleteTask(taskIndex, context!!,widgetId)
        }else if(intent.action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            val ids: IntArray = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)!!
            this.onUpdate(context!!, AppWidgetManager.getInstance(context),ids)
        }
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    fun startDeleteTask(tId:Long, context: Context, wId:Int){
        ioScope.launch {
            deleteTask(tId,context,wId)
        }
    }

    fun deleteTask(tId:Long, context: Context, wId:Int){
        val database = TodoDatabase.getInstance(context)!!.tasksDAO()
        val task = database.get(tId)
        task.done = true;
        database.insert(task);
        updateWidgetInfo(context, wId)
    }

    private fun updateWidgetInfo(context: Context, wId: Int) {
        val awm = AppWidgetManager.getInstance(context)
        awm.notifyAppWidgetViewDataChanged(wId, R.id.widget_list)
    }

}
