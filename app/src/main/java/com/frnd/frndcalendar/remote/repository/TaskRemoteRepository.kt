package com.frnd.frndcalendar.remote.repository

import com.frnd.frndcalendar.remote.model.DeleteTaskRequest
import com.frnd.frndcalendar.remote.model.GetTasksRequest
import com.frnd.frndcalendar.remote.model.GetUserTaskResBody
import com.frnd.frndcalendar.remote.model.StoreTaskRequest
import retrofit2.Response

interface TaskRemoteRepository {
    suspend fun storeCalendarTask(request: StoreTaskRequest): Response<Any>
    suspend fun deleteCalendarTask(request: DeleteTaskRequest): Response<Any>
    suspend fun getCalendarTaskList(request: GetTasksRequest): Response<GetUserTaskResBody>
}