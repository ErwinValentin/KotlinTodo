package com.valentingonzalez.android.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.valentingonzalez.android.R
import com.valentingonzalez.android.database.TaskClass


class CompletedTaskRecyclerListAdapter
internal constructor(context : Context, callback:OnClickCallback): RecyclerView.Adapter<CompletedTaskRecyclerListAdapter.CompletedTaskListViewHolder>(){

    private val inflater : LayoutInflater
    private var todoTaskList: List<TaskClass>? = null
    private var clickCallback: OnClickCallback? = null

    init{
        inflater = LayoutInflater.from(context)
        clickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskListViewHolder {
        val itemView = inflater.inflate(R.layout.task_complete_item,parent,false)
        return CompletedTaskListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if(todoTaskList != null)
            todoTaskList!!.size
        else
            0
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: CompletedTaskListViewHolder, position: Int) {
        if(todoTaskList != null){
            val task = todoTaskList!![position]
            holder.taskDescription.text = holder.itemView.context.getString(R.string.task_description_testing,task.id,task.name)
                //task.id.toString() + " : " +task.name
            holder.taskDueDate.text = task.date.toString()
            //if(task.done) holder.taskDone.isChecked = true
            //if(task.reminder) holder.taskAlarm
        }
    }

    internal fun setList(taskList: List<TaskClass>){
        this.todoTaskList  = taskList
        notifyDataSetChanged()
    }

    inner class CompletedTaskListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val taskDescription: TextView = itemView.findViewById(R.id.taskCompletedDescription)
        val taskDueDate: TextView = itemView.findViewById(R.id.taskCompletedDueDate)
        val taskReschedule : ImageButton = itemView.findViewById(R.id.taskCompletedReschedule)
        val taskDelete : ImageButton = itemView.findViewById(R.id.task_completed_delete)

        init{
            itemView.setOnClickListener(this)
            taskReschedule.setOnClickListener(this)
            taskDelete.setOnClickListener(this)
        }

        override fun onClick(view: View){
            if(clickCallback!= null) {
                if(view == itemView)
                    clickCallback!!.handleClick(adapterPosition)
                if(view == itemView.findViewById( R.id.taskCompletedReschedule))
                    clickCallback!!.handleRescheduleClick(todoTaskList!!.get(adapterPosition))
                if(view == itemView.findViewById( R.id.task_completed_delete))
                    clickCallback!!.handleDeleteClick(todoTaskList!!.get(adapterPosition))
            }
        }

    }
    interface OnClickCallback{
        fun handleClick(position: Int)
        fun handleRescheduleClick(task: TaskClass)
        fun handleDeleteClick(task: TaskClass)
    }
}