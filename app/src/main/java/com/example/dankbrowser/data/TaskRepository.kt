package com.example.dankbrowser.data

import com.example.dankbrowser.data.TaskEntity.Companion.toTask
import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.delete

class TaskRepository {

    private val config = RealmConfiguration(schema = setOf(TaskEntity::class, TabEntity::class))
    private val realm = Realm(config)

    fun getAll(): List<Task> {
        return realm.objects(TaskEntity::class).map { it.toTask() }
    }

    fun addTask(taskEntity: TaskEntity): Task {
        return realm.writeBlocking {
            copyToRealm(taskEntity)
        }.toTask()
    }

    fun deleteTask(task: Task) {
        realm.writeBlocking {
            findLatest(task.originalObject)?.delete()
        }
    }

    fun addTab(task: Task) {
        TODO("Not yet implemented")
    }

    fun removeTab(task: Task, tab: Tab) {
        TODO("Not yet implemented")
    }
}