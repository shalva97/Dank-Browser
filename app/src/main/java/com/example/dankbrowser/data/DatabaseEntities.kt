package com.example.dankbrowser.data

import com.example.dankbrowser.data.TabEntity.Companion.emptyTab
import com.example.dankbrowser.data.TabEntity.Companion.toTab
import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task
import io.realm.RealmList
import io.realm.RealmObject

class TabEntity : RealmObject {
    var url: String = ""
    var contextId: String = ""
    var title: String = ""

    companion object {

        fun TabEntity.toTab(): Tab {
            return Tab(url = url, contextId = contextId, title = title, originalObject = this)
        }

        fun toEntity(tab: Tab): TabEntity {
            return TabEntity().apply {
                url = tab.url
                contextId = tab.contextId
                title = tab.title
            }
        }

        fun emptyTab(): TabEntity {
            return TabEntity().apply {
                url = "http://youtube.com"
                contextId = "Default"
                title = "Blank Tab"
            }
        }
    }

}

class TaskEntity : RealmObject {
    var name: String = ""
    var contextId: String = ""
    var tabs: RealmList<TabEntity> = RealmList()

    companion object {

        fun TaskEntity.toTask(): Task {
            return Task(name, contextId, originalObject = this).apply {
                tabs.forEach { tabEntity ->
                    addTab(tabEntity.toTab())
                }
            }
        }

        fun toEntity(task: Task): TaskEntity {
            val items = task.tabsList.map {
                TabEntity.toEntity(it)
            }

            val realmList = RealmList<TabEntity>()
            realmList.addAll(items)

            return TaskEntity().apply {
                name = task.name
                contextId = task.contextId
                tabs = task.tabsList.map { it.originalObject } as RealmList<TabEntity>
            }
        }

        fun emptyTask(): TaskEntity {
            return TaskEntity().apply {
                name = "Untitled Task"
                this.contextId = "Default"
                tabs.add(emptyTab())
            }
        }
    }
}