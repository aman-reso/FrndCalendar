package com.frnd.frndcalendar.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frnd.frndcalendar.storage.dto.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSingleTask(tasks: TaskEntity)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("INSERT INTO tasks (taskId, title, description, date) SELECT :taskId, :title, :description, :date WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE taskId = :taskId)")
    suspend fun insertTaskIfNotExists(taskId: Int, title: String, description: String, date: String)

    @Query("SELECT * FROM tasks where date=:date")
    fun getAllTaskForParticularDate(date: String): Flow<List<TaskEntity>>


    @Query("SELECT * FROM tasks WHERE date LIKE '%' || :monthYear")
    suspend fun getAllTaskForParticularMonth(monthYear: String): List<TaskEntity>
}