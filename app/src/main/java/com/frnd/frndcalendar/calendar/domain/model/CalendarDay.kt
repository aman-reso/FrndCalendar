package com.frnd.frndcalendar.calendar.domain.model

data class CalendarDay(
    val dayOfMonth: String,
    val month: String,
    val year: String,
    var isSelected: Boolean,
    val eventCount:Int? = null,
)
