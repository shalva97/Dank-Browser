package com.example.dankbrowser.presentation.custom_views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dankbrowser.databinding.AddTaskViewBinding
import com.example.dankbrowser.showKeyboard
import splitties.views.backgroundColor

class EditTextFieldWithConfirmation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var addTaskCallback: (taskName: String) -> Unit
    private lateinit var cancelCallback: () -> Unit
    private val binding = AddTaskViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        with(binding) {
            addBTN.setOnClickListener {
                addTaskCallback.invoke(nameET.text.toString())
                nameET.setText("")
            }

            cancelBTN.setOnClickListener {
                cancelCallback.invoke()
                nameET.setText("")
            }
        }
        backgroundColor = Color.WHITE
    }

    fun onPositive(callback: (taskName: String) -> Unit) {
        addTaskCallback = callback
    }

    fun onCancel(cb: () -> Unit) {
        cancelCallback = cb
    }

    fun showKeyboard() {
        binding.nameET.showKeyboard()
    }

    fun changeHint(hint: String) {
        binding.nameET.hint = hint
    }

}