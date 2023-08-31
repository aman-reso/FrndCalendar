package com.frnd.frndcalendar.calendar.domain

import com.frnd.frndcalendar.calendar.data.CalendarRepository
import com.frnd.frndcalendar.constant.Event
import com.frnd.frndcalendar.calendar.domain.model.SelectedDateModel
import com.frnd.frndcalendar.calendar.domain.model.CalendarDay
import com.frnd.frndcalendar.calendar.domain.model.YearMonthGenerationModel
import javax.inject.Inject

class CalendarUseCase @Inject constructor(private val calendarRepository: CalendarRepository) {
    suspend fun generateCalendarDays(year: Int, month: Int, selectedDateModel: SelectedDateModel): List<CalendarDay> {
        return calendarRepository.generateCalendarDays(year, month, selectedDateModel)
    }
    fun getYearAndMonth(event: Event, currentYear: Int, currentMonth: Int): YearMonthGenerationModel {
        return calendarRepository.getYearAndMonth(event, currentYear, currentMonth)
    }
}

