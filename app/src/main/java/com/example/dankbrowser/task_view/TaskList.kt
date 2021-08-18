package com.example.dankbrowser.task_view

import com.example.dankbrowser.data.TabEntity
import com.example.dankbrowser.data.TaskEntity
import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.task_view.models.ITaskListRVBindings
import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task
import com.example.dankbrowser.task_view.models.rv_types.RVItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TaskList(private val taskRepository: TaskRepository) : ITaskListRVBindings {
    override var list = mutableListOf<Task>()
    private lateinit var dataChangeCallback: () -> Unit
    private val selectedTab = MutableSharedFlow<Tab>(1, 1)

    init {
        val results = taskRepository.getAll()
        list.addAll(results)
    }

    override fun getItemCount(): Int {
        val tabCount = list.map { it.tabsList }.flatten().size
        return list.size * TASK_TITLE_AND_NEW_TAB_BTN + tabCount
    }

    override fun getItemAtIndex(index: Int): RVItem {
        return list.map { task ->
            val tabs = getTabsFromTask(task)
            val taskName = listOf(RVItem.TaskUI(task.name, task))

            taskName + tabs + RVItem.NewTabButton
        }.flatten()[index]
    }

    override fun addOnDataChangedCallback(cb: () -> Unit) {
        dataChangeCallback = cb
    }

    fun addTask(taskName: String) {

        val taskEntity = TaskEntity()
        taskEntity.name = taskName
        taskEntity.contextId = "default"
        taskEntity.tabs.add(
            TabEntity().apply {
                url = "http://youtube.com"
                contextId = taskEntity.contextId
                title = "Blank Tab"
            }
        )

        val task = taskRepository.addTask(taskEntity)

        list.add(task)
        onDataChanged()
    }

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
        list.remove(task)
        onDataChanged()
    }

    fun addTab(task: Task) {

    }

    fun removeTab(task: Task, tab: Tab) {

    }

    fun setSelectedTab(tab: Tab) {
        selectedTab.tryEmit(tab)
    }

    fun getSelectedTab(): SharedFlow<Tab> {
        return selectedTab
    }

    override fun onDataChanged() {
        if (::dataChangeCallback.isInitialized) {
            dataChangeCallback.invoke()
        }
    }

    private fun getTabsFromTask(task: Task): List<RVItem.TabUI> {
        return task.tabsList.map { tab -> RVItem.TabUI(tab.title, tab.url, tab) }
    }

    companion object {
        const val TASK_TITLE_AND_NEW_TAB_BTN = 2
    }
}