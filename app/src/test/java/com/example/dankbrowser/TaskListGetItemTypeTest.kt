package com.example.dankbrowser

import com.example.dankbrowser.task_view.TaskList
import com.example.dankbrowser.task_view.models.*
import com.example.dankbrowser.task_view.models.rv_types.RVItem
import org.junit.Test

class TaskListGetItemTypeTest {

    @Test
    fun `first item is task name`() {
        val tasks = TaskList(tasksDB.tasks())

        tasks.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
            )
        )

        val itemAtIndex = tasks.getItemAtIndex(0)
        assert(itemAtIndex is RVItem.TaskUI)
        assert((itemAtIndex as RVItem.TaskUI).name == "asdf")
    }

    @Test
    fun `if task is empty then it should show new tab button`() {
        val tasks = TaskList(tasksDB.tasks())

        tasks.list.addAll(
            listOf(
                Task("asdf", "default")
            )
        )

        val itemAtIndex = tasks.getItemAtIndex(1)
        println(itemAtIndex)
        assert(itemAtIndex is RVItem.NewTabButton)
    }

    @Test
    fun `given a task with 1 tab then it should return a tab at index 1`() {
        val tasks = TaskList(tasksDB.tasks())

        tasks.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                },
            )
        )

        val itemAtIndex = tasks.getItemAtIndex(1)
        assert(itemAtIndex is RVItem.TabUI)
        assert((itemAtIndex as RVItem.TabUI).name == "youtttube titlea")
    }

    @Test
    fun `given a task with 1 tab then it should return a new tab button at index 2`() {
        val tasks = TaskList(tasksDB.tasks())

        tasks.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                },
            )
        )

        val itemAtIndex = tasks.getItemAtIndex(2)
        assert(itemAtIndex is RVItem.NewTabButton)

    }

    @Test
    fun `test correct items for more than one task`() {

        val tasks = TaskList(tasksDB.tasks())

        tasks.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
            )
        )

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