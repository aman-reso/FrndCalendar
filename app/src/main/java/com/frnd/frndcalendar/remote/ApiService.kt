package com.frnd.frndcalendar.remote

import com.frnd.frndcalendar.remote.model.StoreTaskRequest
import com.frnd.frndcalendar.remote.model.GetUserTaskResBody
import com.frnd.frndcalendar.remote.model.DeleteTaskRequest
import com.frnd.frndcalendar.remote.model.GetTasksRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("api/storeCalendarTask")
    suspend fun storeCalendarTask(@Body request: StoreTaskRequest): Any

    @POST("api/deleteCalendarTask")
    suspend fun deleteCalendarTask(@Body request: DeleteTaskRequest): Any

    @POST("api/getCalendarTaskList")
    suspend fun getCalendarTaskList(@Body request: GetTasksRequest): GetUserTaskResBody
}