package com.example.dankbrowser.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dankbrowser.databinding.AddTaskViewBinding


class EditTextFieldWithConfirmation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var addTaskCallback: (taskName: String) -> Unit
    private lateinit var cancelCallback: () -> Unit

    init {
        val binding = AddTaskViewBinding.inflate(LayoutInflater.from(context), this)

        with(binding) {
            addBTN.setOnClickListener {
                addTaskCallback.invoke(taskNameET.text.toString())
                taskNameET.setText("")
            }

            cancelBTN.setOnClickListener {
                cancelCallback.invoke()
                taskNameET.setText("")
            }
        }
    }

    fun onAddTask(callback: (taskName: String) -> Unit) {
        addTaskCallback = callback
    }

    fun onCancel(cb: () -> Unit) {
        cancelCallback = cb
    }

}