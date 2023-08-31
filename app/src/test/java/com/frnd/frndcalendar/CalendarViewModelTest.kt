package com.frnd.frndcalendar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.frnd.frndcalendar.calendar.domain.CalendarUseCase
import com.frnd.frndcalendar.calendar.domain.model.CalendarDay
import com.frnd.frndcalendar.constant.Event
import com.frnd.frndcalendar.domain.TaskUseCase
import com.frnd.frndcalendar.presentation.uiState.TaskUiState
import com.frnd.frndcalendar.presentation.viewmodel.CalendarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class CalendarViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var calendarUseCase: CalendarUseCase

    @Mock
    private lateinit var taskUseCase: TaskUseCase

    @Mock
    private lateinit var calendarDaysObserver: Observer<List<CalendarDay>>

    @Mock
    private lateinit var userTasksObserver: Observer<TaskUiState>

    private lateinit var viewModel: CalendarViewModel

    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CalendarViewModel(calendarUseCase, taskUseCase)
        viewModel.calendarDays.observeForever(calendarDaysObserver)
        viewModel.userTasks.observeForever(userTasksObserver)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun advanceTimeBy(delayMillis: Long) {
        testDispatcher.scheduler.apply { advanceTimeBy(delayMillis); runCurrent() }
    }

    @Test
    fun `generateCalendarDays should update calendarDays and userTasks`() = testDispatcher.runBlockingTest {
        // Given
        val fakeCalendarDays = listOf(CalendarDay("1", "1", "2023", false, null))
        val fakeEvent = Event.DEFAULT
        viewModel.generateCalendarDays(fakeEvent)
        verify(calendarDaysObserver).onChanged(fakeCalendarDays)
        verify(userTasksObserver).onChanged(any())
    }

}
