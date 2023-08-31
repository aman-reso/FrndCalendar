package com.frnd.frndcalendar.remote.model

import androidx.annotation.Keep

@Keep
data class GetUserTaskResBody(
    val tasks: List<TaskResponse>? = arrayListOf(),
)