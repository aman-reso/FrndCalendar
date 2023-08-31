package com.frnd.frndcalendar.utility

import com.frnd.frndcalendar.calendar.domain.model.SelectedDateModel

class CalendarUtils {
    companion object {
        internal fun isSameDate(selectedDateModel: SelectedDateModel, day: Int, month: Int, year: Int): Boolean {
            return selectedDateModel.selectedDate == day && selectedDateModel.selectedMonth == month && selectedDateModel.selectedYear == year
        }

        internal fun generateCustomDateFormat(selectedDate: Int, defaultMonth: Int, defaultYear: Int): String {
            return "$selectedDate-$defaultMonth-$defaultYear"
        }

        val monthAbbreviations = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
    }
}