package com.example.dankbrowser

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dankbrowser.domain.Tab
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TabTests {

    @Test
    fun addTab() {
        val taskList = getNewTaskList()

        taskList.addTask("69")

        // by default there is one tab created
        val myTask = taskList.getTasks().first()
        assert(myTask.tabsList.size == 1)

        taskList.addTab(myTask)

        assert(myTask.tabsList.size == 2)
    }

    @Test
    fun removeTab() {
        // will remove tab that is created by default
        val taskList = getNewTaskList()

        taskList.addTask("every 60 seconds one minute passes")

        val myTask = taskList.getTasks().first()

        taskList.removeTab(myTask, myTask.tabsList.first())

        assert(myTask.tabsList.isEmpty())
    }

    @Test
    fun changeTabUrl() {
        // will change url of the tab in the first task
        val taskList = getNewTaskList()

        taskList.addTask("TDD is the only true religion")

        val myTask = taskList.getTasks().first()
        val myTab = myTask.tabsList.first()
        val newWebsite = "https://waifu.pics"

        myTab.saveUrl(newWebsite)

        assert((myTab.url.value as Tab.Url.Website).url == newWebsite)

    }

    @Test
    fun loadUrlIsBeingCalled() {
        val taskList = getNewTaskList()

        taskList.addTask("paperrrrrrrrrr")

        val myTask = taskList.getTasks().first()
        val myTab = myTask.tabsList.first()
        val newWebsite = "https://waifu.pics"

        myTab.saveUrl(newWebsite)


    }
}