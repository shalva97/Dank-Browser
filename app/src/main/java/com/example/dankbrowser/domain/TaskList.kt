package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.presentation.task_view.models.ITaskListRVBindings
import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.annotations.TestOnly

class TaskList(private val taskRepository: TaskRepository) : ITaskListRVBindings {
    private var list = mutableListOf<Task>()
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

            taskName + tabs + RVItem.NewTabButton(task)
        }.flatten()[index]
    }

    override fun addOnDataChangedCallback(cb: () -> Unit) {
        dataChangeCallback = cb
    }

    fun addTask(taskName: String) {

        val task = taskRepository.addTask(taskName)

        list.add(task)
        onDataChanged()
    }

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
        list.remove(task)
        onDataChanged()
    }

    fun addTab(task: Task) {
        val tab = taskRepository.addTab(task)
        task.tabsList.add(tab)
        onDataChanged()
    }

    fun removeTab(task: Task, tab: Tab) {
        taskRepository.removeTab(tab)
        task.tabsList.remove(tab)
        onDataChanged()
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

    @TestOnly
    fun getTasks(): MutableList<Task> {
        return list
    }

    private fun getTabsFromTask(task: Task): List<RVItem.TabUI> {
        return task.tabsList.map { tab -> RVItem.TabUI(tab.title, tab.url, tab, task) }
    }

    companion object {
        const val TASK_TITLE_AND_NEW_TAB_BTN = 2
    }
}