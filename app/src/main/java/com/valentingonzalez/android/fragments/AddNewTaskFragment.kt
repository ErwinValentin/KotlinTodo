package com.valentingonzalez.android.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat.getDateInstance
import java.util.*


class AddNewTaskFragment: DialogFragment() {
    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog!!.window?.setLayout(width, height)
        }
    }

    var listener: AddTaskInterface? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(com.valentingonzalez.android.R.layout.add_task_fragment, container, false)
        this.dialog?.setTitle("Add New Task")

        val tilDesc = rootView.findViewById<TextInputLayout>(com.valentingonzalez.android.R.id.til_task_description)
        val tilDate = rootView.findViewById<TextInputLayout>(com.valentingonzalez.android.R.id.til_task_date)
        val taskDescription = rootView.findViewById<TextView>(com.valentingonzalez.android.R.id.tv_task_description)
        val taskDate = rootView.findViewById<TextView>(com.valentingonzalez.android.R.id.tv_task_date)
        val cbReminder = rootView.findViewById<CheckBox>(com.valentingonzalez.android.R.id.cb_reminder)
        val btnCancel = rootView.findViewById<MaterialButton>(com.valentingonzalez.android.R.id.cancel_add_button)
        val btnAccept = rootView.findViewById<MaterialButton>(com.valentingonzalez.android.R.id.add_task_button)

        btnCancel.setOnClickListener {
            Toast.makeText(activity, "action cancelled", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnAccept.setOnClickListener {
            Toast.makeText(activity, "User Accepted Action", Toast.LENGTH_SHORT).show()
            var err = false
            if(taskDescription.text.isEmpty()) {
                tilDesc.error = "Please add a description"
                err = true

            }
            if(taskDate.text.isEmpty()){
                tilDate.error = "Please select a date"
                err = true
            }else{
                //val shortdateFormat = SimpleDateFormat("MM/dd/yyyy")
                val shortdateFormat = getDateInstance()
                var convertedDate: Date? = Date()
                try{
                    convertedDate = shortdateFormat.parse(taskDate.text.toString())
                    tilDate.error = ""
                }catch (e: Exception){
                    tilDate.error = "Please enter a valid date"
                    err = true
                }
            }

            if(!err){
                listener?.AddTaskOk(taskDescription.text.toString(),taskDate.text.toString(),cbReminder.isChecked)
                dismiss()
            }

        }

        return rootView

    }

    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString("content")

        // Pick a style based on the num.
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as AddTaskInterface
    }

    interface AddTaskInterface{
        fun AddTaskOk(description:String, date:String, reminder:Boolean)
    }
}