package com.example.dankbrowser

import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task
import com.example.dankbrowser.task_view.TaskList
import org.junit.Test


class TaskListGetItemCountTest {

    @Test
    fun `return zero if no tasks are created`() {
        val taskList = TaskList()

        assert(taskList.getItemCount() == 0)
    }

    @Test
    fun `total number of rv items`() {
        val taskList = TaskList()

        taskList.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
            )
        )

        assert(taskList.getItemCount() == 4)
    }

    @Test
    fun `more tab number tests`() {
        val taskList = TaskList()

        taskList.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
            )
        )

        assert(taskList.getItemCount() == 9)
    }

    @Test
    fun `even more tab number tests`() {
        val taskList = TaskList()

        taskList.list.addAll(
            listOf(
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                    addTab(Tab("http://facebook.com", contextId, "youtttube titlea"))
                },
                Task("asdf", "default").apply {
                    addTab(Tab("http://youtube.com", contextId, "youtttube titlea"))
                },
            )
        )

        assert(taskList.getItemCount() == 17)
    }
}