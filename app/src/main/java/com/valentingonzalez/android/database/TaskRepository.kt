package com.valentingonzalez.android.database

import android.app.Application
import androidx.lifecycle.LiveData

class TaskRepository(application: Application) {
    private val taskDao: TaskDAO
    private val notCompletedLiveData: LiveData<List<TaskClass>>
    private val completedLiveData: LiveData<List<TaskClass>>
    private val reminderListData: List<TaskClass>

    init{
        val taskDatabase = TodoDatabase.getInstance(application)
        taskDao = taskDatabase?.tasksDAO()!!
        completedLiveData = taskDao.getCompleted()
        notCompletedLiveData = taskDao.getNotCompleted()
        reminderListData = taskDao.getReminders()
    }

    fun getNotCompleted():LiveData<List<TaskClass>>{
        return notCompletedLiveData
    }
    fun getCompleted():LiveData<List<TaskClass>>{
        return completedLiveData
    }
    fun getReminders():List<TaskClass>{
        return reminderListData
    }
    fun get(id: Long):TaskClass{
        return taskDao.get(id)
    }
    fun insert(task: TaskClass):Long{
        return taskDao.insert(task)
    }
    fun delete(task: TaskClass){
       taskDao.delete(task)
    }
    fun deleteAll(){
        taskDao.deleteAll()
    }
}