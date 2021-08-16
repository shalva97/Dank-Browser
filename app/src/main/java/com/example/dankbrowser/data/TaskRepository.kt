package com.example.dankbrowser.data

import com.example.dankbrowser.task_view.models.Task
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.toFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class TaskRepository {

    @Volatile
    private var realmDatabase: Realm
    private val databaseThread = CoroutineScope(Dispatchers.Default).coroutineContext
//    private val myScope = CoroutineScope(Dispatchers.IO)

    init {
        runBlocking(databaseThread) {
            val config = RealmConfiguration.Builder()
                .name(REALM_NAME)
                .build()
            println(this::class.simpleName + " Thread: " + Thread.currentThread().id)
            realmDatabase = Realm.getInstance(config)
        }
    }

    fun getAll(): Flow<List<Task>> {
        return runBlocking(databaseThread) {
            println(this::class.simpleName + " getAll - Thread: " + Thread.currentThread().id)
            realmDatabase.where(TaskEntity::class.java).findAll()
                .toFlow()
                .map {
                    it.toList().map { taskEntity -> taskEntity.toTask() }
                }
        }
    }

    fun addTask(taskEntity: TaskEntity) {
        runBlocking(databaseThread) {
            println(this::class.simpleName + " addTask - Thread: " + Thread.currentThread().id)
            realmDatabase.executeTransaction {
                it.insert(taskEntity)
            }
        }
    }

    fun deleteTask(task: Task) {
        runBlocking(databaseThread) {
            println(this::class.simpleName + " deleteTask - Thread: " + Thread.currentThread().id)
            realmDatabase.executeTransaction {
                task.originalObject.deleteFromRealm()
            }
        }
    }

    companion object {
        private const val REALM_NAME: String = "DankApplication"
    }
}