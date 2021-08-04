package com.example.dankbrowser.task_view.models

data class Task(val name: String, val contextId: String) {
    val tabsList = mutableListOf<Tab>()

    fun addTab(tab: Tab) {
        tabsList.add(tab)
    }

}

