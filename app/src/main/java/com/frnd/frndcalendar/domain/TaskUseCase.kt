package com.frnd.frndcalendar.domain

import androidx.room.Transaction
import com.frnd.frndcalendar.remote.model.StoreTaskRequest
import com.frnd.frndcalendar.remote.model.DeleteTaskRequest
import com.frnd.frndcalendar.remote.model.GetTasksRequest
import com.frnd.frndcalendar.remote.repository.TaskRemoteRepository
import com.frnd.frndcalendar.storage.repository.TaskLocalRepository
import com.frnd.frndcalendar.storage.dto.TaskEntity
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val remoteRepository: TaskRemoteRepository, private val taskRepository: TaskLocalRepository,
) {
    suspend fun storeCalendarTask(request: StoreTaskRequest) = remoteRepository.storeCalendarTask(request)

    suspend fun deleteCalendarTask(request: DeleteTaskRequest) = remoteRepository.deleteCalendarTask(request)

    suspend fun getCalendarTaskList(request: GetTasksRequest) = remoteRepository.getCalendarTaskList(request)

    suspend fun deleteTaskFromLocal(taskId: Int) = taskRepository.deleteTask(taskId)
    suspend fun insertSingleTaskIntoLocal(task: TaskEntity) = taskRepository.insertSingleTask(task)

    suspend fun insertTasksIntoLocal(tasks: List<TaskEntity>) =taskRepository.insertTasks(tasks)

    fun getAllTaskForParticularDate(date: String) = taskRepository.getAllTaskForParticularDate(date)

     suspend fun getAllTaskForParticularMonth(monthYear: String) = taskRepository.getAllTaskForParticularMonth(monthYear)

}
