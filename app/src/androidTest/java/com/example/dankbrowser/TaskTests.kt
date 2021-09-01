package com.example.dankbrowser

import org.junit.Test


class TaskTests {

    @Test
    fun changeContextName() {
        val taskList = getNewTaskList()

        taskList.addTask("no hopes for this app")

        val task = taskList.getTasks().first()
        val contextName = "sixnain"
        taskList.changeContext(task, contextName)
    }
}