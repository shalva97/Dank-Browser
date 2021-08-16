package com.example.dankbrowser.data

import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task
import io.realm.RealmList
import io.realm.RealmObject

open class TabEntity(
    var url: String = "",
    var contextId: String = "",
    var title: String = ""
) : RealmObject() {

    fun toTab(): Tab {
        return Tab(url = url, contextId = contextId, title = title, originalObject = this)
    }

    companion object {
        fun toEntity(tab: Tab): TabEntity {
            return TabEntity(tab.url, tab.contextId, tab.title)
        }
    }

}

open class TaskEntity(
    var name: String = "",
    var contextId: String = "",
    var tabs: RealmList<TabEntity> = RealmList()
) : RealmObject() {

    fun toTask(): Task {
        return Task(name, contextId, originalObject = this).apply {
            tabs.forEach { tabEntity ->
                addTab(tabEntity.toTab())
            }
        }
    }

    companion object {
        fun toEntity(task: Task): TaskEntity {
            val items = task.tabsList.map {
                TabEntity.toEntity(it)
            }

            val realmList = RealmList<TabEntity>()
            realmList.addAll(items)

            return TaskEntity(task.name, task.contextId, realmList)
        }
    }
}