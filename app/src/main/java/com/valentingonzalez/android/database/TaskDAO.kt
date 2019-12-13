package com.valentingonzalez.android.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface TaskDAO {

    @Query("SELECT * from tasks")
    fun getAll(): List<TaskClass>

    @Query("SELECT * from tasks WHERE done = 0")
    fun getNotCompleted(): LiveData<List<TaskClass>>

    @Query("SELECT * from tasks WHERE done = 0")
    fun getNotCompletedAsList():List<TaskClass>

    @Query("SELECT * from tasks WHERE done = 1")
    fun getCompleted(): LiveData<List<TaskClass>>

    @Query("SELECT * from tasks WHERE reminder = 1 AND done = 0")
    fun getReminders(): List<TaskClass>

    @Query("SELECT * from tasks WHERE id = :id")
    fun get(id:Long): TaskClass

    @Insert(onConflict = REPLACE)
    fun insert(task: TaskClass):Long

    @Delete
    fun delete(task: TaskClass):Int

    @Query("DELETE from tasks")
    fun deleteAll()
}