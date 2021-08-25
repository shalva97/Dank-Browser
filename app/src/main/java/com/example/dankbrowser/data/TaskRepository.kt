package com.example.dankbrowser.data

import com.example.dankbrowser.data.TabEntity.Companion.toTab
import com.example.dankbrowser.data.TaskEntity.Companion.toTask
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task
import io.realm.Realm
import io.realm.delete

class TaskRepository(private val realm: Realm) {

    fun getAll(): List<Task> {
        return realm.objects(TaskEntity::class).map { it.toTask() }
    }

    fun addTask(taskName: String): Task {

        val taskEntity = TaskEntity.emptyTask()
        taskEntity.name = taskName

        return realm.writeBlocking {
            copyToRealm(taskEntity)
        }.toTask()
    }

    fun deleteTask(task: Task) {
        realm.writeBlocking {
            findLatest(task.originalObject)?.delete()
        }
    }

    fun addTab(task: Task): Tab {
        return realm.writeBlocking {
            val tab = copyToRealm(TabEntity.emptyTab())
            findLatest(task.originalObject)?.tabs?.add(tab)
            copyToRealm(task.originalObject)
        }.toTask().tabsList.last()
    }

    fun removeTab(tab: Tab) {
        realm.writeBlocking {
            findLatest(tab.originalObject)?.delete()
        }
    }

    fun changeUrl(selectedTab: Tab, url: String): Tab {
        val tabEntity = realm.writeBlocking {
            findLatest(selectedTab.originalObject)?.apply {
                this.url = url
            }
        }

        val newElement = tabEntity!!.toTab()
        if (selectedTab.isInitialized()) {
            newElement.geckoSession = selectedTab.geckoSession
        }

        return newElement

    }
}
