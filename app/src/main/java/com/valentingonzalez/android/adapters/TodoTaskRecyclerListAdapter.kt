package com.valentingonzalez.android.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.valentingonzalez.android.R
import com.valentingonzalez.android.database.TaskClass

class TodoTaskRecyclerListAdapter
internal constructor(context : Context, callback: OnClickCallback): RecyclerView.Adapter<TodoTaskRecyclerListAdapter.TodoTaskListViewHolder>(){

    /*TODO
        if a task is past due, change the background or smtng
    * */
    private val inflater : LayoutInflater
    private var todoTaskList: List<TaskClass>? = null
    private var clickCallback: OnClickCallback? = null

    init{
        inflater = LayoutInflater.from(context)
        clickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoTaskListViewHolder {
        val itemView = inflater.inflate(R.layout.task_item,parent,false)
        return TodoTaskListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if(todoTaskList != null)
            todoTaskList!!.size
        else
            0
    }

    override fun onBindViewHolder(holder: TodoTaskListViewHolder, position: Int) {
        if(todoTaskList != null){
            val task = todoTaskList!![position]
            holder.taskDescription.text = holder.itemView.context.getString(R.string.task_description_testing,task.id,task.name)
            holder.taskDueDate.text = task.date.toString()
            if(task.done) holder.taskDone.isChecked = true
            //if(task.reminder) holder.taskAlarm

            if(todoTaskList?.get(position)!!.reminder)
                holder.taskAlarm.setImageResource(R.drawable.ic_alarm_on_black_24dp)
            else
                holder.taskAlarm.setImageResource(R.drawable.ic_alarm_off_black_24dp)

            holder.taskAlarm.setOnClickListener{
                if(todoTaskList?.get(position)!!.reminder) {
                    todoTaskList?.get(position)!!.reminder = false
                    holder.taskAlarm.setImageResource(R.drawable.ic_alarm_off_black_24dp)
                }else {
                    todoTaskList?.get(position)!!.reminder = true
                    holder.taskAlarm.setImageResource(R.drawable.ic_alarm_on_black_24dp)
                }
                clickCallback!!.toggleReminder(todoTaskList!!.get(position))
            }
        }
    }

    internal fun setList(taskList: List<TaskClass>){
        this.todoTaskList  = taskList
        notifyDataSetChanged()
    }

    inner class TodoTaskListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener{

        val taskDescription: TextView = itemView.findViewById(R.id.task_description_text_view)
        val taskDueDate: TextView = itemView.findViewById(R.id.taskDueDateTextView)
        val taskDone : CheckBox = itemView.findViewById(R.id.task_done_checkbox)
        val taskAlarm : ImageButton = itemView.findViewById(R.id.task_toggle_alarm_button)

        init{
            itemView.setOnClickListener(this)

            taskDone.setOnCheckedChangeListener {
                    buttonView, isChecked ->
                    clickCallback!!.handleChecked(todoTaskList!!.get(adapterPosition))
                    taskDone.isChecked = false
            }
        }

        override fun onClick(view:View){
            Log.d("ADAPTER",view.id.toString())
            if(clickCallback != null) {
                if(view == itemView)
                    clickCallback!!.handleClick(adapterPosition)
            }
        }

    }
    interface OnClickCallback{
        fun handleClick(position: Int)
        fun handleChecked(task: TaskClass)
        fun toggleReminder(task: TaskClass)
    }
}


