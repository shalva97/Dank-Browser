package com.example.dankbrowser.presentation.gecko_fragment.small_task_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dankbrowser.components
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.TaskList
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class SmallTaskListFragmentViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    private val selectedTab = taskList.selectedTask.selectedTab
    val selectedTask = taskList.selectedTask
    val pageTitle = selectedTab.title.asLiveData(viewModelScope.coroutineContext)
    val url = selectedTab.url.filter { it is Tab.Url.Website }
        .map { (it as Tab.Url.Website).url }
        .asLiveData(viewModelScope.coroutineContext)
    val taskName = taskList.selectedTask.name
    val tabs = taskList.selectedTask.tabsList

    fun goBack() {
        selectedTab.geckoEngineSession.goBack()
    }

    fun goForward() {
        selectedTab.geckoEngineSession.goForward()
    }

    fun reload() {
        selectedTab.geckoEngineSession.reload()
    }
}