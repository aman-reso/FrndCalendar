package com.frnd.frndcalendar.remote.repository

import com.frnd.frndcalendar.remote.model.StoreTaskRequest
import com.frnd.frndcalendar.remote.model.DeleteTaskRequest
import com.frnd.frndcalendar.remote.model.GetTasksRequest
import com.frnd.frndcalendar.remote.ApiService
import javax.inject.Inject

class TaskRemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) :TaskRemoteRepository{
    override suspend fun storeCalendarTask(request: StoreTaskRequest) =
        apiService.storeCalendarTask(request)

   override suspend fun deleteCalendarTask(request: DeleteTaskRequest) =
        apiService.deleteCalendarTask(request)

    override suspend fun getCalendarTaskList(request: GetTasksRequest) =
        apiService.getCalendarTaskList(request)
}