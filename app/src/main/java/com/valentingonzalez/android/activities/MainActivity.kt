package com.valentingonzalez.android.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.valentingonzalez.android.R
import com.valentingonzalez.android.adapters.TaskPagerAdapter
import com.valentingonzalez.android.database.TaskClass
import com.valentingonzalez.android.database.TaskViewModel
import com.valentingonzalez.android.fragments.AddNewTaskFragment
import com.valentingonzalez.android.fragments.TodoCompletedFragment
import com.valentingonzalez.android.fragments.TodoMainFragment
import com.valentingonzalez.android.notifications.NotificationManagerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), AddNewTaskFragment.AddTaskInterface {
    private var TAB_NUMBER = 2
    private lateinit var viewPager:ViewPager;
    private lateinit var tabLayout:TabLayout;
    var taskViewModel: TaskViewModel? = null
    var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel::class.java)

        viewPager = findViewById(R.id.main_tab_viewpager)
        tabLayout = findViewById(R.id.main_tab_tabList)

        val fragmentAdapter = TaskPagerAdapter(supportFragmentManager)
        fragmentAdapter.addItem(TodoMainFragment(),getString(R.string.todo_tab_label))
        fragmentAdapter.addItem(TodoCompletedFragment(),getString(R.string.completed_tab_label))

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> openSettings()
            R.id.action_about -> openAbout()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
    fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

    }
    fun openAbout(){
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
    fun addNewTask(view: View){
        val fm = supportFragmentManager
        val addFragment = AddNewTaskFragment()
        addFragment.show(fm,"Add New Task")
    }
    override fun AddTaskOk(description: String, date: String, reminder: Boolean) {
        Snackbar.make(findViewById<ConstraintLayout>(R.id.main_content),"Desc: "+description+" Date: "+date+" Remind: "+reminder,Snackbar.LENGTH_LONG).show()
        val task = TaskClass(null, description,date, reminder,false)
        uiScope.launch {
            insertNewTask(task)
        }
    }
    fun insertNewTask(task: TaskClass){
        val tid:Long = taskViewModel!!.insert(task)
        task.id = tid
        scheduleNotification(task)
    }


    fun scheduleNotification(task: TaskClass){
        if(task.reminder){
            var time: Long = 0
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            time = dateFormat.parse(task.date).time
            Log.d("MAIN",task.id.toString())
            Log.d("MAIN",task.name)
            Log.d("MAIN",time.toString())
            NotificationManagerService().sendNotification(task.id!!,task.name,time, this)
        }
    }
}
