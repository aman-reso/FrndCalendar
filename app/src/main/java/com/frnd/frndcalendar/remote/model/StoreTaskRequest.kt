package com.frnd.frndcalendar.remote.model

data class StoreTaskRequest(
    val user_id: Int,
    val task: Task
)

data class Task(
    val title: String,
    val description: String
)