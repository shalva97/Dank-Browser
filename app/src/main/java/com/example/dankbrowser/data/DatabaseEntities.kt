package com.example.dankbrowser.data

import com.example.dankbrowser.data.TabEntity.Companion.emptyTab
import com.example.dankbrowser.data.TabEntity.Companion.toTab
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.realmListOf
import mozilla.components.concept.engine.Engine
import java.util.*

class TabEntity : RealmObject {
    var url: String = ""
    var contextId: String = ""
    var title: String = ""

    companion object {

        fun TabEntity.toTab(realm: Realm, engine: Engine): Tab {
            return Tab(
                originalObject = this,
                realm = realm,
                engine = engine
            )
        }

        fun emptyTab(contextId: String): TabEntity {
            return TabEntity().apply {
                url = ""
                this.contextId = contextId
                title = "Blank Tab"
            }
        }
    }

}

class TaskEntity : RealmObject {
    var name: String = ""
    var contextId: String = ""
    var tabs: RealmList<TabEntity> = realmListOf()

    companion object {

        fun TaskEntity.toTask(realm: Realm, geckoRuntime: Engine): Task {
            return Task(name, contextId, originalObject = this).apply {
                tabs.forEach { tabEntity ->
                    addTab(tabEntity.toTab(realm, geckoRuntime))
                }
            }
        }

        fun emptyTask(): TaskEntity {
            return TaskEntity().apply {
                name = "Untitled Task"
                this.contextId = UUID.randomUUID().toString()
                tabs.add(emptyTab(contextId))
            }
        }
    }
}