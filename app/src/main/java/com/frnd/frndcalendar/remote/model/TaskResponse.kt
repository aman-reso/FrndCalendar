package com.frnd.frndcalendar.remote.model

import androidx.annotation.Keep
import com.frnd.frndcalendar.remote.model.TaskDetail

@Keep
data class TaskResponse(
    val task_detail: TaskDetail? = null,
    val task_id: Int,
)