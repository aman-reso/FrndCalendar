package com.frnd.frndcalendar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frnd.frndcalendar.R
import com.frnd.frndcalendar.remote.model.TaskResponse

class TaskAdapter(private val taskItemListener: TaskItemListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<TaskResponse>() {
        override fun areItemsTheSame(oldItem: TaskResponse, newItem: TaskResponse): Boolean {
            return oldItem.task_id == newItem.task_id // Change to the appropriate identifier in your Task class
        }

        override fun areContentsTheSame(oldItem: TaskResponse, newItem: TaskResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffCallback)

    var taskResponseList: List<TaskResponse>
        get() = asyncListDiffer.currentList
        set(value) = asyncListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view, taskItemListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskResponseList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskResponseList.size
    }

    class TaskViewHolder(itemView: View, private val taskItemListener: TaskItemListener) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.taskTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.taskDescriptionTextView)
        private val deleteTaskImgView: ImageView = itemView.findViewById(R.id.deleteTaskImageView)
        internal fun bind(taskResponse: TaskResponse) {
            titleTextView.text = taskResponse.task_detail?.title
            descriptionTextView.text = taskResponse.task_detail?.description
            deleteTaskImgView.setOnClickListener {
                taskItemListener.didTapOnDeleteTask(taskId = taskResponse.task_id)
            }
        }
    }

    interface TaskItemListener {
        fun didTapOnDeleteTask(taskId: Int)
    }
}
