package com.valentingonzalez.android.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class TaskViewModel(application: Application) :  AndroidViewModel(application){
    private var tasksRepository: TaskRepository? = null
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init{
        uiScope.launch {
            tasksRepository = TaskRepository(application)
        }
    }

    suspend fun getCompleted():LiveData<List<TaskClass>>?{
        return withContext(Dispatchers.IO){
            tasksRepository!!.getCompleted()
        }
    }
    suspend fun getNotCompleted():LiveData<List<TaskClass>>?{
        return withContext(Dispatchers.IO){
            tasksRepository!!.getNotCompleted()
        }

    }
    fun getReminders():List<TaskClass>?{
        return tasksRepository!!.getReminders()
    }
    fun get(id: Long): TaskClass{
        return tasksRepository!!.get(id)
    }
    fun insert(task: TaskClass):Long{
        return tasksRepository?.insert(task)!!
    }
    fun delete(task: TaskClass){
        tasksRepository?.delete(task)
    }
    fun deleteAll(){
        tasksRepository?.deleteAll()
    }
}