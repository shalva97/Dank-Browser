package com.example.dankbrowser.data

import com.example.dankbrowser.task_view.models.Task
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TaskRepository {

    init {

        val config = RealmConfiguration.Builder()
            .name(REALM_NAME)
            .build()
        Realm.setDefaultConfiguration(config)
//        println(this::class.simpleName + " Thread: " + Thread.currentThread().id)


    }

    fun getAll(): List<Task> {
//        println(this::class.simpleName + " getAll - Thread: " + Thread.currentThread().id)
        return runBlocking(Dispatchers.IO) {
            val realmDatabase = Realm.getDefaultInstance()

            realmDatabase.where(TaskEntity::class.java).findAll()
                .map {
                    it.toTask()
                }
        }
    }

    fun addTask(taskEntity: TaskEntity) {
//        println(this::class.simpleName + " addTask - Thread: " + Thread.currentThread().id)
        runBlocking(Dispatchers.IO) {
            val realmDatabase = Realm.getDefaultInstance()
            realmDatabase.executeTransaction {
                it.insert(taskEntity)
            }
        }
    }

    fun deleteTask(task: Task) {
//        println(this::class.simpleName + " deleteTask - Thread: " + Thread.currentThread().id)
        runBlocking(Dispatchers.IO) {
            val realmDatabase = Realm.getDefaultInstance()
            realmDatabase.executeTransaction {
                task.originalObject.deleteFromRealm()
            }
        }
    }

    companion object {
        private const val REALM_NAME: String = "DankApplication"
    }
}