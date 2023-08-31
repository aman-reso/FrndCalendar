package com.frnd.frndcalendar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frnd.frndcalendar.remote.model.StoreTaskRequest
import com.frnd.frndcalendar.remote.model.Task
import com.frnd.frndcalendar.domain.TaskUseCase
import com.frnd.frndcalendar.constant.Event
import com.frnd.frndcalendar.constant.Metadata.Companion.USER_ID
import com.frnd.frndcalendar.calendar.domain.CalendarUseCase
import com.frnd.frndcalendar.calendar.domain.model.CalendarDay
import com.frnd.frndcalendar.remote.model.DeleteTaskRequest
import com.frnd.frndcalendar.remote.model.GetTasksRequest
import com.frnd.frndcalendar.calendar.domain.model.SelectedDateModel
import com.frnd.frndcalendar.presentation.uiState.TaskUiState
import com.frnd.frndcalendar.remote.model.TaskDetail
import com.frnd.frndcalendar.remote.model.TaskResponse
import com.frnd.frndcalendar.storage.dto.TaskEntity
import com.frnd.frndcalendar.utility.CalendarUtils.Companion.generateCustomDateFormat
import com.frnd.frndcalendar.utility.CalendarUtils.Companion.isSameDate
import com.frnd.frndcalendar.utility.CalendarUtils.Companion.monthAbbreviations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarUseCase: CalendarUseCase,
    private val taskUseCase: TaskUseCase,
) : ViewModel() {

    private val _calendarDays = MutableLiveData<List<CalendarDay>>()
    val calendarDays: LiveData<List<CalendarDay>> = _calendarDays

    private val _userTasksUiState = MutableLiveData<TaskUiState>()
    val userTasks: LiveData<TaskUiState> get() = _userTasksUiState

    private var job: Job? = null

    private var latestCalendarDayCache: List<CalendarDay>? = null

    //used for calculation Purpose
    internal var defaultYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var defaultMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var defaultDate: Int = Calendar.getInstance().get(Calendar.DATE)

    //this is used for date selection purpose
    private var selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var selectedDate: Int = Calendar.getInstance().get(Calendar.DATE)

    //if true then calendar will not show event count along with date
    private val canByPassCalendarWithEvent: Boolean = true

    init {
        getCalendarTaskList(false)
    }

    internal fun generateCalendarDays(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        calendarUseCase.getYearAndMonth(event, defaultYear, defaultMonth).apply {
            defaultMonth = this.currentMonth
            defaultYear = this.currentYear
            latestCalendarDayCache = calendarUseCase.generateCalendarDays(year = defaultYear, month = defaultMonth, SelectedDateModel(selectedDate, selectedMonth, selectedYear))
            updateCalendarWithTaskCount(false)
            getTaskForParticularDate(generateCustomDateFormat(selectedDate, defaultMonth, defaultYear))
        }
    }


    internal fun createTaskAtServerAndUpdateLocal(title: String, description: String) = viewModelScope.launch(Dispatchers.IO) {
        _userTasksUiState.postValue(TaskUiState.Empty(true))
        val inputData = StoreTaskRequest(USER_ID, Task(title, description))
        val response = taskUseCase.storeCalendarTask(inputData)
        if (response.isSuccessful) {
            response.body()?.let {
                getCalendarTaskList(true)
            } ?: run {
                _userTasksUiState.postValue(TaskUiState.Error(false, "Something went wrong"))
            }
        } else {
            _userTasksUiState.postValue(TaskUiState.Error(false, "Something went wrong"))
        }
    }


    internal fun deleteTaskAtServerAndLocal(taskId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _userTasksUiState.postValue(TaskUiState.Empty(true))
        val response = taskUseCase.deleteCalendarTask(DeleteTaskRequest(USER_ID, taskId))
        if (response.isSuccessful) {
            response.body()?.let {
                taskUseCase.deleteTaskFromLocal(taskId)
            } ?: run {
                _userTasksUiState.postValue(TaskUiState.Error(false, "Something went wrong"))
            }
        } else {
            _userTasksUiState.postValue(TaskUiState.Error(false, "Something went wrong"))
        }
    }

    //isNewlyCreated if true then recently added task and need to sync with local db
    //generally it will be last one
    private fun getCalendarTaskList(isNewlyCreated: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val response = taskUseCase.getCalendarTaskList(GetTasksRequest(USER_ID))
        if (response.isSuccessful) {
            response.body()?.tasks?.let {
                if (isNewlyCreated) {
                    val recentCreatedTask = it.last()
                    taskUseCase.insertSingleTaskIntoLocal(TaskEntity(taskId = recentCreatedTask.task_id,
                        title = recentCreatedTask.task_detail?.title ?: "",
                        description = recentCreatedTask.task_detail?.description ?: "",
                        date = generateCustomDateFormat(selectedDate, selectedMonth, selectedYear)))
                } else {
                    taskUseCase.insertTasksIntoLocal(it.map { data ->
                        TaskEntity(taskId = data.task_id,
                            title = data.task_detail?.title ?: "",
                            description = data.task_detail?.description ?: "",
                            date = generateCustomDateFormat(defaultDate, defaultMonth, defaultYear))
                    })
                }
            } ?: run {
                _userTasksUiState.postValue(TaskUiState.Error(false, "Something went wrong"))
            }
        } else {
            _userTasksUiState.postValue(TaskUiState.Error(false, "Something went wrong"))
        }
    }

    internal fun getMonthName(): String {
        return monthAbbreviations[defaultMonth]
    }

    internal fun updateCalendarBasedOnDateSelection(calendarDay: CalendarDay) = viewModelScope.launch(Dispatchers.IO) {
        if (calendarDay.year != "" && calendarDay.month != "" && calendarDay.dayOfMonth != "") {
            selectedYear = calendarDay.year.toInt()
            selectedMonth = calendarDay.month.toInt()
            selectedDate = calendarDay.dayOfMonth.toInt()
            val selectedDateModel = SelectedDateModel(selectedDate, selectedMonth, selectedYear)
            latestCalendarDayCache = latestCalendarDayCache?.map {
                if (it.dayOfMonth != "" && it.month != "" && it.year != "") {
                    it.copy(isSelected = isSameDate(
                        selectedDateModel,
                        it.dayOfMonth.toInt(),
                        it.month.toInt(),
                        it.year.toInt()
                    ))
                } else {
                    it.copy(isSelected = false)
                }
            }
            updateCalendarWithTaskCount(false)
            getTaskForParticularDate(date = generateCustomDateFormat(selectedDate, selectedMonth, selectedYear))
        }
    }

    private fun getTaskForParticularDate(date: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            taskUseCase.getAllTaskForParticularDate(date).collectLatest { collectedData ->
                val mappedData: List<TaskResponse> = collectedData.map { data ->
                    TaskResponse(task_detail = TaskDetail(data.description, data.title), task_id = data.taskId)
                }
                _userTasksUiState.postValue(TaskUiState.Success(mappedData, false))
            }
        }
    }

    private suspend fun updateCalendarWithTaskCount(canByPassCalendarWithEvent: Boolean) {
        if (canByPassCalendarWithEvent) {
            latestCalendarDayCache?.let {
                _calendarDays.postValue(it)
            }
        } else {
            val taskListOfReqMonthYear = taskUseCase.getAllTaskForParticularMonth("$defaultMonth-$defaultYear")
            if (taskListOfReqMonthYear.isEmpty()) {
                latestCalendarDayCache?.let {
                    _calendarDays.postValue(it)
                }
            } else {
                val updatedCalendarDays = latestCalendarDayCache?.map { calendarDay ->
                    if (calendarDay.month != "" && calendarDay.year != "") {
                        val dayOfMonth = calendarDay.dayOfMonth.toInt()
                        val month = calendarDay.month.toInt()
                        val matchingTasks = taskListOfReqMonthYear.filter { taskEntity ->
                            val parts = taskEntity.date.split("-")
                            val taskDay = parts[0].toInt()
                            val taskMonth = parts[1].toInt()
                            dayOfMonth == taskDay && month == taskMonth
                        }
                        calendarDay.copy(eventCount = matchingTasks.size)
                    } else {
                        calendarDay
                    }
                }
                updatedCalendarDays?.let { _calendarDays.postValue(it) }
            }
        }
    }

}