package com.example.dankbrowser.domain

import com.example.dankbrowser.presentation.task_view.models.ITaskListRVBindings
import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem

class TaskListRVBindings(private val list: List<Task>) : ITaskListRVBindings {

    private lateinit var dataChangeCallback: () -> Unit

    override fun getItemCount(): Int {
        val tabCount = list.map { it.tabsList }.flatten().size
        return list.size * TASK_TITLE_AND_NEW_TAB_BTN + tabCount
    }

    override fun getItemAtIndex(index: Int): RVItem {
        return list.map { task ->
            val tabs = getTabsFromTask(task)
            val taskName = listOf(RVItem.TaskUI(task.name, task))

            taskName + tabs + RVItem.NewTabButton(task)
        }.flatten()[index]
    }

    private fun getTabsFromTask(task: Task): List<RVItem.TabUI> {
        return task.tabsList.map { tab -> RVItem.TabUI(tab.title, tab, task) }
    }

    override fun onDataChanged() {
        if (::dataChangeCallback.isInitialized) {
            dataChangeCallback.invoke()
        }
    }

    override fun addOnDataChangedCallback(cb: () -> Unit) {
        dataChangeCallback = cb
    }

    companion object {
        const val TASK_TITLE_AND_NEW_TAB_BTN = 2
    }
}