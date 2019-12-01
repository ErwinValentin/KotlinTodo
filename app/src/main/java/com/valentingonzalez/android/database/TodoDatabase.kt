package com.valentingonzalez.android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TaskClass::class), version = 1)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun tasksDAO():TaskDAO

    companion object {
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase? {
            if (INSTANCE == null) {
                synchronized(TodoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TodoDatabase::class.java, "tasks.db")
                        //.allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}