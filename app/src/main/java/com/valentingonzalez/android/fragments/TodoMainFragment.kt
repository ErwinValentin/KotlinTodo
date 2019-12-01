package com.valentingonzalez.android.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.valentingonzalez.android.R
import com.valentingonzalez.android.adapters.TodoTaskRecyclerListAdapter
import com.valentingonzalez.android.database.TaskClass
import com.valentingonzalez.android.database.TaskViewModel
import com.valentingonzalez.android.notifications.NotificationManagerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class TodoMainFragment : Fragment(), TodoTaskRecyclerListAdapter.OnClickCallback{
    val TaskModel: TaskClass? =null;
    var adapter: TodoTaskRecyclerListAdapter? = null
    var taskViewModel: TaskViewModel? = null
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val insertScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        adapter = TodoTaskRecyclerListAdapter(requireContext(),this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        getNotCompletedTasks(adapter!!)
        /*var list = ArrayList<TaskClass>()
        list.add(TaskClass(1,"a",Calendar.getInstance().time.toString(),true,false))
        list.add(TaskClass(2,"b",Calendar.getInstance().time.toString(),false,true))
        list.add(TaskClass(3,"c",Calendar.getInstance().time.toString(),true,false))
        adapter.setList(list)*/
    }

    override fun handleClick(position: Int) {
        Toast.makeText(requireContext(),"Not Implemented!",Toast.LENGTH_SHORT).show()
        Log.d("CLICK","reached click")
    }

    override fun handleChecked(task: TaskClass) {
        insertScope.launch{
            isCompleted(task)
            //TODO this snackbar flickers
            Snackbar.make(requireView(),task.toString(), Snackbar.LENGTH_LONG).show()
        }

    }

    override fun toggleReminder(task: TaskClass) {
        Log.d("MAIN REMIND", "reached toggle")
        Log.d("MAIN",task.toString())
        if(task.reminder){
            var time: Long = 0
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            time = dateFormat.parse(task.date).time
            NotificationManagerService().sendNotification(task.id!!,task.name,time, requireActivity())
            insertScope.launch {
                taskViewModel!!.insert(task)
                //getTasks(adapter!!)
            }
        }else{

            NotificationManagerService().cancelNotification(task.id!!)
            insertScope.launch {
                taskViewModel!!.insert(task)
                //getTasks(adapter!!)
            }
        }

    }
    suspend fun isCompleted(task: TaskClass){
        task.done = true
        if(task.reminder) {
            Toast.makeText(requireContext(), "task must be removed from alarms", Toast.LENGTH_SHORT).show()
        }
        task.reminder = false
        taskViewModel!!.insert(task)
        getTasks(adapter!!)
    }

    fun getNotCompletedTasks(adapter: TodoTaskRecyclerListAdapter){
        uiScope.launch {
            getTasks(adapter)
        }
    }
    suspend fun getTasks(adapter: TodoTaskRecyclerListAdapter){
        taskViewModel?.getNotCompleted()?.observe(this, object: Observer<List<TaskClass>> {
            override fun onChanged(t: List<TaskClass>?) {
                adapter.setList(t!!)
            }
        })
    }

}


