package com.valentingonzalez.android.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.valentingonzalez.android.R
import com.valentingonzalez.android.database.TaskClass

class WidgetListAdapter(context: Context, var list: List<TaskClass>) : ArrayAdapter<TaskClass>(context, 0,list ){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.widget_list_item, parent,false)

        val check = item.findViewById<CheckBox>(R.id.widget_checkbox)
        check.setOnCheckedChangeListener{
            button, b ->
            //TODO Call repo and update list, use another callback?
        }
        //TODO GET ITEM
        val currTask = list[position]


        return item
    }
}