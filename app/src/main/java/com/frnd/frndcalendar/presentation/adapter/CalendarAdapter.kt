package com.frnd.frndcalendar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frnd.frndcalendar.R
import com.frnd.frndcalendar.calendar.domain.model.CalendarDay


class CalendarAdapter(private val dateSelectionListener: DateSelectionListener) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem.dayOfMonth == newItem.dayOfMonth
        }

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    internal fun setCalendarDays(days: List<CalendarDay>) {
        differ.submitList(days)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell, parent, false)
        return CalendarViewHolder(itemView, dateSelectionListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val calendarDay = differ.currentList[position]
        holder.bind(calendarDay)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class CalendarViewHolder(itemView: View, private val dateSelectionListener: DateSelectionListener) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.calendarDateTv)
        private val eventCountTv: TextView = itemView.findViewById(R.id.eventCountTv)
        internal infix fun bind(calendarDay: CalendarDay) {
            if (calendarDay.isSelected) {
                dayTextView.text = calendarDay.dayOfMonth
                dayTextView.setBackgroundResource(R.drawable.blue_selected_bg)
            } else {
                dayTextView.text = calendarDay.dayOfMonth
                dayTextView.background = null
            }
            if (calendarDay.eventCount == null || calendarDay.eventCount == 0) {
                eventCountTv.text = ""
            } else {
                eventCountTv.text = calendarDay.eventCount.toString()
            }
            dayTextView.setOnClickListener {
                dateSelectionListener.didTapOnDate(calendarDay)
            }
        }
    }

    interface DateSelectionListener {
        fun didTapOnDate(calendarDay: CalendarDay)
    }
}

