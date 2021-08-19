package com.example.dankbrowser

import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem
import org.junit.Test

class TaskListGetItemTypeTest {

    @Test
    fun first_item_in_taskView() {
        val taskList = getNewTaskList().apply {
            addTask("asdf")
            addTab(getTasks()[0])
        }

        val itemAtIndex = taskList.getItemAtIndex(0)

        assert(itemAtIndex is RVItem.TaskUI)
        assert((itemAtIndex as RVItem.TaskUI).name == "asdf")
    }

    @Test
    fun ifTaskListIsEmptyItShouldShowNewTabButton() {
        val tasks = getNewTaskList().apply {
            addTask("asdf")
            removeTab(
                getTasks().first(),
                getTasks().first().tabsList.first()
            )
        }

        val itemAtIndex = tasks.getItemAtIndex(1)
        println(itemAtIndex)
        assert(itemAtIndex is RVItem.NewTabButton)
    }

    @Test
    fun givenOneTaskItShouldReturnATabAtIndexOne() {
        val tasks = getNewTaskList()

        tasks.addTask("asdf")

        val itemAtIndex = tasks.getItemAtIndex(1)
        assert(itemAtIndex is RVItem.TabUI)
        assert((itemAtIndex as RVItem.TabUI).name == "Blank Tab")
    }

    @Test
    fun oneTaskShouldReturnNewTabButtonAtIndexTwo() {
        val tasks = getNewTaskList()

        tasks.addTask("Iam awesome")

        val itemAtIndex = tasks.getItemAtIndex(2)
        assert(itemAtIndex is RVItem.NewTabButton)

    }

    @Test
    fun testCorrectItemsForMoreThanOneTask() {

        val tasks = getNewTaskList()

        tasks.addTask("the best task in the existence")
        tasks.addTask("worst task")

        tasks.addTab(tasks.getTasks()[1])

        var itemAtIndex = tasks.getItemAtIndex(0)
        assert(itemAtIndex is RVItem.TaskUI)
        itemAtIndex = tasks.getItemAtIndex(1)
        assert(itemAtIndex is RVItem.TabUI)
        itemAtIndex = tasks.getItemAtIndex(2)
        assert(itemAtIndex is RVItem.NewTabButton)
        itemAtIndex = tasks.getItemAtIndex(3)
        assert(itemAtIndex is RVItem.TaskUI)
        itemAtIndex = tasks.getItemAtIndex(4)
        assert(itemAtIndex is RVItem.TabUI)
        itemAtIndex = tasks.getItemAtIndex(5)
        assert(itemAtIndex is RVItem.TabUI)
        itemAtIndex = tasks.getItemAtIndex(6)
        assert(itemAtIndex is RVItem.NewTabButton)
    }
}