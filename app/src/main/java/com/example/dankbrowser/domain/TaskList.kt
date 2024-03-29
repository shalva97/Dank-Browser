package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.presentation.task_view.models.ITaskListRVBindings
import org.jetbrains.annotations.TestOnly

class TaskList(
    private val taskRepository: TaskRepository,
    private var list: MutableList<Task> = mutableListOf()
) : ITaskListRVBindings by TaskListRVBindings(list) {

    lateinit var selectedTask: Task
        private set

    init {
        val results = taskRepository.getAll()
        list.addAll(results)
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

    fun setSelectedTab(tab: Tab, task: Task) {
        task.selectedTab = tab
        selectedTask = task
    }

    @TestOnly
    fun getTasks(): MutableList<Task> {
        return list
    }

//    fun changeUrl(selectedTab: Tab, url: String, task: Task): Tab {
//        val index = task.tabsList.indexOf(selectedTab)
//        val newElement = taskRepository.changeUrl(selectedTab, url)
//
//        task.tabsList.removeAt(index)
//        task.tabsList.add(index, newElement)
//        task.selectedTab = newElement
//
//        this.selectedTask = task
//        return newElement
//    }

    fun changeContext(task: Task, contextName: String) {
        // savee task and objects in it

        // first variant
        // save data as usual and remap everything  one by one

        // make task fields mutable and update directly

        // look at other projects and imitate
    }
}