package com.frnd.frndcalendar.storage.repository

import androidx.room.Transaction
import com.frnd.frndcalendar.storage.dao.TaskDao
import com.frnd.frndcalendar.storage.dto.TaskEntity
import javax.inject.Inject


class TaskLocalRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskLocalRepository {

    override fun getAllUserLocalTasks() = taskDao.getAllTasks()

    @Transaction
    override suspend fun insertTasks(tasks: List<TaskEntity>) {
        tasks.forEach { task ->
            taskDao.insertTaskIfNotExists(
                taskId = task.taskId,
                title = task.title,
                description = task.description,
                date = task.date
            )
        }
//        taskDao.insertTasks(tasks)
    }

    override suspend fun insertSingleTask(task: TaskEntity) {
        taskDao.insertSingleTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId = taskId)
    }

    override fun getAllTaskForParticularDate(date: String) = taskDao.getAllTaskForParticularDate(date)

    override suspend fun insertSingleTaskIfNotExists(taskId: Int, title: String, description: String, date: String) {
        taskDao.insertTaskIfNotExists(taskId, title, description, date)
    }

    override suspend fun getAllTaskForParticularMonth(monthYear: String) = taskDao.getAllTaskForParticularMonth(monthYear)

}