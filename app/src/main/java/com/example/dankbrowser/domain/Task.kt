package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TaskEntity

data class Task(val name: String, val contextId: String, val originalObject: TaskEntity) {
    val tabsList = mutableListOf<Tab>()
    lateinit var selectedTab: Tab

    fun addTab(tab: Tab) {
        tabsList.add(tab)
    }

}
