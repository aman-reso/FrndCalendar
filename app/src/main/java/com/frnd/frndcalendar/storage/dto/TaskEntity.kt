package com.frnd.frndcalendar.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val taskId: Int,
    val title: String,
    val description: String,
    val date: String,
)