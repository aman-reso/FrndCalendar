package com.frnd.frndcalendar.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.frnd.frndcalendar.constant.Event
import com.frnd.frndcalendar.constant.Metadata.Companion.GRID_COLUMN
import com.frnd.frndcalendar.databinding.CalendarHomeBinding
import com.frnd.frndcalendar.calendar.domain.model.CalendarDay
import com.frnd.frndcalendar.extension.showBottomSheetIsNotPresent
import com.frnd.frndcalendar.presentation.adapter.CalendarAdapter
import com.frnd.frndcalendar.presentation.viewmodel.CalendarViewModel
import com.frnd.frndcalendar.presentation.adapter.TaskAdapter
import com.frnd.frndcalendar.presentation.bottomsheet.CreateTaskBtmSheet
import com.frnd.frndcalendar.presentation.bottomsheet.CreateTaskBtmSheet.Companion.TAG
import com.frnd.frndcalendar.presentation.uiState.TaskUiState
import dagger.hilt.android.AndroidEntryPoint


//we can also make calendar using viewpager2
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskAdapter.TaskItemListener, CalendarAdapter.DateSelectionListener, CreateTaskBtmSheet.CreateTaskBtmSheetCallback {
    private val viewModel: CalendarViewModel by viewModels()
    private val calendarAdapter: CalendarAdapter by lazy { CalendarAdapter(this) }
    private var binding: CalendarHomeBinding? = null
    private val taskAdapter: TaskAdapter by lazy { TaskAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.customCalendarRV?.apply {
            adapter = calendarAdapter
            layoutManager = GridLayoutManager(this@MainActivity, GRID_COLUMN)
        }
        setUpGestureForSwipe()
        //task recyclerview
        binding?.taskRecyclerView?.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        setUpClickEvents()
        setUpObservers()
        viewModel.generateCalendarDays(event = Event.DEFAULT)
    }


    private fun setUpClickEvents() {
        binding?.nextMonthIcon?.setOnClickListener {
            viewModel.generateCalendarDays(event = Event.NEXT)
        }
        binding?.prevMonthIcon?.setOnClickListener {
            viewModel.generateCalendarDays(event = Event.PREV)
        }
        binding?.createTaskFabIcon?.setOnClickListener {
            val btmSheet=CreateTaskBtmSheet.newInstance()
            supportFragmentManager.showBottomSheetIsNotPresent(btmSheet,TAG)
        }
    }

    private fun setUpObservers() {
        viewModel.calendarDays.observe(this) { days ->
            calendarAdapter.setCalendarDays(days)
            updateMonthYearText()
        }

        viewModel.userTasks.observe(this) {
            parseUserTaskResponse(it)
        }
    }

    private fun parseUserTaskResponse(taskUiState: TaskUiState) {
        if (taskUiState.isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
        when (taskUiState) {
            is TaskUiState.Error -> {
                Toast.makeText(this, taskUiState.errorMessage, Toast.LENGTH_LONG).show()
            }

            is TaskUiState.Success -> {
                taskAdapter.taskResponseList = taskUiState.data
            }

            is TaskUiState.Empty -> {
                //leave it
            }
        }
    }

    private fun updateMonthYearText() {
        binding?.currentMonthTitle?.text = buildString {
            append(viewModel.getMonthName())
            append(" ")
            append(viewModel.defaultYear)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun didTapOnDeleteTask(taskId: Int) {
        viewModel.deleteTaskAtServerAndLocal(taskId)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpGestureForSwipe() {
        val gestureDetector = GestureDetectorCompat(this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                    val dx = e2.x.minus(e1.x)
                    if (dx > 30) {
                        viewModel.generateCalendarDays(event = Event.PREV)
                        return true
                    } else if (dx < -30) {
                        viewModel.generateCalendarDays(event = Event.NEXT)
                        return true
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            }
        )
        binding?.customCalendarRV?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun didTapOnDate(calendarDay: CalendarDay) {
        viewModel.updateCalendarBasedOnDateSelection(calendarDay)
    }

    override fun createTask(title: String, description: String) {
        viewModel.createTaskAtServerAndUpdateLocal(title, description)
    }
}
