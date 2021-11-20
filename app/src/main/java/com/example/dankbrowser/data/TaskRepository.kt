package com.example.dankbrowser.data

import com.example.dankbrowser.data.TaskEntity.Companion.toTask
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task
import io.realm.Realm
import io.realm.delete
import org.mozilla.geckoview.GeckoRuntime

class TaskRepository(private val realm: Realm, val geckoRuntime: GeckoRuntime) {

    fun getAll(): List<Task> {
        return realm.objects(TaskEntity::class).map { it.toTask(realm, geckoRuntime) }
    }

    fun addTask(taskName: String): Task {

        val taskEntity = TaskEntity.emptyTask()
        taskEntity.name = taskName

        return realm.writeBlocking {
            copyToRealm(taskEntity)
        }.toTask(realm, geckoRuntime)
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
        }.toTask(realm, geckoRuntime).tabsList.last()
    }

    fun removeTab(tab: Tab) {
        realm.writeBlocking {
//            findLatest(tab.originalObject)?.delete() // TODO
        }
    }
}
