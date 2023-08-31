package com.frnd.frndcalendar.calendar.data

import com.frnd.frndcalendar.calendar.domain.model.CalendarDay
import com.frnd.frndcalendar.calendar.domain.model.SelectedDateModel
import com.frnd.frndcalendar.utility.CalendarUtils.Companion.isSameDate
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepositoryImpl @Inject constructor() : CalendarRepository {
    override suspend fun generateCalendarDays(year: Int, month: Int, selectedDateModel: SelectedDateModel): List<CalendarDay> {
        val calendarDays = mutableListOf<CalendarDay>()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        dayLabels.forEach {
            calendarDays.add(CalendarDay(it, "", "", false))
        }

        for (i in 1 until firstDayOfWeek) {
            val emptyDay = CalendarDay("", "", "", false)
            calendarDays.add(emptyDay)
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (dayOfMonth in 1..daysInMonth) {
            val isSelectedDate = isSameDate(selectedDateModel, dayOfMonth, month, year)
            val calendarDay = CalendarDay(dayOfMonth.toString(), month.toString(), year = year.toString(), isSelectedDate)
            calendarDays.add(calendarDay)
        }
        return calendarDays
    }
    
}