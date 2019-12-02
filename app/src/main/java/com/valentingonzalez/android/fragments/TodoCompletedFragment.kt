package com.valentingonzalez.android.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.valentingonzalez.android.R
import com.valentingonzalez.android.adapters.CompletedTaskRecyclerListAdapter
import com.valentingonzalez.android.database.TaskClass
import com.valentingonzalez.android.database.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoCompletedFragment : Fragment(), CompletedTaskRecyclerListAdapter.OnClickCallback{
    val TaskModel: TaskClass? =null
    var taskViewModel: TaskViewModel? = null
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val insertScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private var adapter: CompletedTaskRecyclerListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CompletedTaskRecyclerListAdapter(requireContext(),this)
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        getCompletedTasks(adapter!!)
        /*var list = ArrayList<TaskClass>()

        list.add(TaskClass(1,"a", Calendar.getInstance().time.toString(),true,false))
        list.add(TaskClass(2,"b", Calendar.getInstance().time.toString(),true,false))
        list.add(TaskClass(3,"c", Calendar.getInstance().time.toString(),true,false))
        adapter!!.setList(list)*/
    }
    fun getCompletedTasks(adapter: CompletedTaskRecyclerListAdapter){
        uiScope.launch {
            getTasks(adapter)
        }
    }
    suspend fun getTasks(adapter: CompletedTaskRecyclerListAdapter){
        taskViewModel?.getCompleted()?.observe(this, object: Observer<List<TaskClass>> {
            override fun onChanged(t: List<TaskClass>?) {
                adapter.setList(t!!)
            }
        })
    }

    override fun handleClick(position: Int) {
        Toast.makeText(requireContext(),"Not Implemented!", Toast.LENGTH_SHORT).show()
    }

    override fun handleRescheduleClick(task: TaskClass) {
        //Toast.makeText(requireContext(),"Reschedule Not Implemented!", Toast.LENGTH_SHORT).show()
        uiScope.launch {
            rescheduleTask(task)
        }
    }

    suspend fun rescheduleTask(task: TaskClass){
        //TODO start edit fragment, set a callback to receive the new task's date and reminder
        task.done = false
        //TODO if need reminder, then
        /* task.date = new date
            if(rescheduledTaskReminder  == true)
            task.reminder = true
            send new alarm
        * */

        taskViewModel!!.insert(task)
        getTasks(adapter!!)
    }

    override fun handleDeleteClick(task: TaskClass) {
        //Toast.makeText(requireContext(),"Delete Not Implemented!", Toast.LENGTH_SHORT).show()
        //TODO show confirmation dialog
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Confirm Delete Task")
        builder.setMessage("Are you sure you want to Delete: "+task.name)
        builder.setPositiveButton("YES")
        { dialogInterface, i ->
            insertScope.launch {
                deleteTask(task)
            }
        }
        builder.setNegativeButton("NO")
        { dialogInterface, i ->
            Toast.makeText(requireActivity(),"Delete Canceled",Toast.LENGTH_SHORT).show()
        }
        val dialog = builder.create()
        dialog.show()

    }

    fun deleteTask(task: TaskClass){
        taskViewModel!!.delete(task)
        //getTasks(adapter!!)
    }
}
