package com.valentingonzalez.android.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskClass(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: String?,
    @ColumnInfo(name = "reminder") var reminder: Boolean,
    @ColumnInfo(name = "done") var done: Boolean){

    constructor():this(null,"","",false, false)

    override fun toString(): String {
        return "id: $id name: $name date: $date reminder?: $reminder done?: $done"
    }
}