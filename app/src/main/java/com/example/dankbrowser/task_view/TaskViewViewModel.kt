package com.example.dankbrowser.task_view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.example.dankbrowser.R
import com.example.dankbrowser.task_view.models.ITaskListRVBindings
import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class TaskViewViewModel @Inject constructor(
    application: Application,
    private val taskList: TaskList
) : AndroidViewModel(application) {

    val navigation = MutableSharedFlow<Int>(extraBufferCapacity = 1)

    fun addTask(taskName: String) {
        taskList.addTask(taskName)
    }


//    init {
//        taskList.taskList.addAll(
//            listOf(
//                Task("asdf", "default").apply {
//                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
//                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
//                },
//                Task("asdf another", "default").apply {
//                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
//                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
//                    addTab(Tab("http://facebook.com", contextId, "facebook titlea"))
//                },
//            )
//        )
//    }

    fun getRVData(): ITaskListRVBindings {
        return taskList
    }

    fun deleteTask(task: Task) {
        taskList.deleteTask(task)
    }

    fun switchToTab(tab: Tab) {
        taskList.setSelectedTab(tab)
        navigation.tryEmit(R.id.action_taskView_to_geckoFragment)
    }
}