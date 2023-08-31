package com.frnd.frndcalendar.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frnd.frndcalendar.storage.dao.TaskDao
import com.frnd.frndcalendar.storage.dto.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}