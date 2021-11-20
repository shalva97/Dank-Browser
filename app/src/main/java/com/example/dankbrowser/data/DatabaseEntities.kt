package com.example.dankbrowser.data

import com.example.dankbrowser.data.TabEntity.Companion.emptyTab
import com.example.dankbrowser.data.TabEntity.Companion.toTab
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.realmListOf
import org.mozilla.geckoview.GeckoRuntime

class TabEntity : RealmObject {
    var url: String = ""
    var contextId: String = ""
    var title: String = ""

    companion object {

        fun TabEntity.toTab(realm: Realm, geckoRuntime: GeckoRuntime): Tab {
            return Tab(
                originalObject = this,
                realm = realm,
                geckoRuntime = geckoRuntime
            )
        }

        fun emptyTab(): TabEntity {
            return TabEntity().apply {
                url = ""
                contextId = "Default"
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

        fun TaskEntity.toTask(realm: Realm, geckoRuntime: GeckoRuntime): Task {
            return Task(name, contextId, originalObject = this).apply {
                tabs.forEach { tabEntity ->
                    addTab(tabEntity.toTab(realm, geckoRuntime))
                }
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