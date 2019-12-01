package com.valentingonzalez.android.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.valentingonzalez.android.R
import com.valentingonzalez.android.database.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsActivity: AppCompatActivity() {

    var taskViewModel: TaskViewModel? = null
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
    }

    fun deleteAll(view: View) {
        uiScope.launch {
            deleteAllTasks()
        }
    }

    fun deleteAllTasks(){
        taskViewModel!!.deleteAll()
    }
}