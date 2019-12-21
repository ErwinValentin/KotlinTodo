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
import java.text.DateFormat.getDateInstance


class TodoMainFragment : Fragment(), TodoTaskRecyclerListAdapter.OnClickCallback{
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
    }

    override fun handleClick(position: Int) {
        Toast.makeText(requireContext(),"Not Implemented!",Toast.LENGTH_SHORT).show()
        Log.d("CLICK","reached click")
    }

    override fun handleChecked(task: TaskClass) {
        insertScope.launch{
            isCompleted(task)
            Snackbar.make(requireView(),task.toString(), Snackbar.LENGTH_LONG).show()
        }

    }

    override fun toggleReminder(task: TaskClass) {
        if(task.reminder){
            val dateFormat = getDateInstance()
            val str = task.date!!
            val time = dateFormat.parse(str)?.time
            //var time: Long = 0
            //val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            //time = dateFormat.parse(task.date).time
            NotificationManagerService().sendNotification(task.id!!,task.name,time!!, requireActivity())
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
    private suspend fun isCompleted(task: TaskClass){
        task.done = true
        NotificationManagerService().cancelNotification(task.id!!)
        task.reminder = false
        taskViewModel!!.insert(task)
//        getTasks(adapter!!)
    }

    private fun getNotCompletedTasks(adapter: TodoTaskRecyclerListAdapter){
        uiScope.launch {
            getTasks(adapter)
        }
    }
    private suspend fun getTasks(adapter: TodoTaskRecyclerListAdapter){
        taskViewModel?.getNotCompleted()?.observe(this, object: Observer<List<TaskClass>> {
            override fun onChanged(t: List<TaskClass>?) {
                adapter.setList(t!!)
            }
        })
    }

}


