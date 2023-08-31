package com.frnd.frndcalendar.calendar.data

import com.frnd.frndcalendar.constant.Event
import com.frnd.frndcalendar.calendar.domain.model.SelectedDateModel
import com.frnd.frndcalendar.calendar.domain.model.CalendarDay
import com.frnd.frndcalendar.calendar.domain.model.YearMonthGenerationModel
import java.util.Calendar

interface CalendarRepository {
    suspend fun generateCalendarDays(year: Int, month: Int, selectedDateModel: SelectedDateModel): List<CalendarDay>

    fun getYearAndMonth(event: Event, currentYear: Int, currentMonth: Int): YearMonthGenerationModel {
        var localMonth = currentMonth
        var localYear = currentYear
        when (event) {
            Event.NEXT -> {
                if (localMonth == Calendar.DECEMBER) {
                    localMonth = Calendar.JANUARY
                    localYear++
                } else {
                    localMonth++
                }
            }

            Event.PREV -> {
                if (localMonth == Calendar.JANUARY) {
                    localMonth = Calendar.DECEMBER
                    localYear--
                } else {
                    localMonth--
                }
            }

            Event.DEFAULT -> {
                YearMonthGenerationModel(localYear, localMonth)
            }
        }
        return YearMonthGenerationModel(localYear, localMonth)
    }
}