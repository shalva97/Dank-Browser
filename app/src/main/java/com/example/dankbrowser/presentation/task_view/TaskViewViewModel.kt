package com.example.dankbrowser.presentation.task_view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dankbrowser.R
import com.example.dankbrowser.components
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task
import com.example.dankbrowser.domain.TaskList
import com.example.dankbrowser.getString
import com.example.dankbrowser.presentation.task_view.models.ITaskListRVBindings
import kotlinx.coroutines.flow.MutableSharedFlow

class TaskViewViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    val navigation = MutableSharedFlow<Int>(extraBufferCapacity = 1)

    fun addTask(taskName: String) {
        if (taskName.isNotEmpty()) {
            taskList.addTask(taskName)
        } else {
            taskList.addTask(getString(R.string.common_untitled))
        }
    }

    fun getTaskListDataForRV(): ITaskListRVBindings {
        return taskList
    }

    fun deleteTask(task: Task) {
        taskList.deleteTask(task)
    }

    fun switchToTab(tab: Tab, task: Task) {
        taskList.setSelectedTab(tab, task)
        navigation.tryEmit(R.id.action_taskView_to_geckoFragment)
    }

    fun createNewTab(task: Task) {
        taskList.addTab(task)
    }

    fun deleteTab(it: Pair<Task, Tab>) {
        taskList.removeTab(it.first, it.second)
    }

    fun changeContext(it: String) {

    }
}