package com.example.dankbrowser

import org.junit.Test


class TaskListGetItemCountTest {

    @Test
    fun returnZeroIfNoTasksAreCreated() {
        val taskList = getNewTaskList()

        assert(taskList.getItemCount() == 0)
    }

    @Test
    fun totalNumberOfRvItems() {
        val taskList = getNewTaskList()
        taskList.addTask("blah")

        assert(taskList.getItemCount() == 3)
    }

    @Test
    fun anotherTabCountTest() {
        val taskList = getNewTaskList()

        taskList.addTask("blah")
        taskList.addTask("blah2")

        taskList.addTab(taskList.getTasks()[0])
        taskList.addTab(taskList.getTasks()[1])
        taskList.addTab(taskList.getTasks()[1])

        assert(taskList.getItemCount() == 9)
    }

    @Test
    fun evenMoreItemCountTest() {
        val taskList = getNewTaskList()

        taskList.addTask("blah")
        taskList.addTab(taskList.getTasks()[0])

        taskList.addTask("blah2")
        taskList.addTab(taskList.getTasks()[1])
        taskList.addTab(taskList.getTasks()[1])

        taskList.addTask("blah2")
        taskList.addTab(taskList.getTasks()[2])
        taskList.addTab(taskList.getTasks()[2])

        taskList.addTask("blah2")

        assert(taskList.getItemCount() == 17)
    }
}