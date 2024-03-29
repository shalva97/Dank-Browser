package com.example.dankbrowser

import androidx.test.platform.app.InstrumentationRegistry
import com.example.dankbrowser.data.TabEntity
import com.example.dankbrowser.data.TaskEntity
import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.domain.TaskList
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.objects

val components = Components(
    InstrumentationRegistry.getInstrumentation().targetContext
)

fun getNewTaskList(): TaskList {
    val realm by lazy {
        val config = RealmConfiguration.with(schema = setOf(TaskEntity::class, TabEntity::class))
        Realm.open(config).also {
            it.writeBlocking {
                objects<TaskEntity>().delete()
                objects<TabEntity>().delete()
            }
        }
    }

    val realmDatabase by lazy {
        TaskRepository(realm)
    }


    return TaskList(realmDatabase)

}