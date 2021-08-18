package com.example.dankbrowser.task_view.models

import com.example.dankbrowser.data.TaskEntity

data class Task(val name: String, val contextId: String, val originalObject: TaskEntity) {
    val tabsList = mutableListOf<Tab>()

    fun addTab(tab: Tab) {
        tabsList.add(tab)
    }

}
