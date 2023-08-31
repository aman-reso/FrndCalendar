package com.frnd.frndcalendar.storage.repository

import com.frnd.frndcalendar.storage.dto.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskLocalRepository {
    fun getAllUserLocalTasks(): Flow<List<TaskEntity>>
    suspend fun insertTasks(tasks: List<TaskEntity>)
    suspend fun insertSingleTask(task: TaskEntity)
    suspend fun deleteTask(taskId: Int)
    fun getAllTaskForParticularDate(date: String): Flow<List<TaskEntity>>
    suspend fun insertSingleTaskIfNotExists(taskId: Int, title: String, description: String, date: String)

    suspend fun getAllTaskForParticularMonth(monthYear: String):List<TaskEntity>
}