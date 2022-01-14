package com.example.dankbrowser

import android.app.Application
import android.content.Context
import com.example.dankbrowser.data.TabEntity
import com.example.dankbrowser.data.TaskEntity
import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.domain.TaskList
import io.realm.Realm
import io.realm.RealmConfiguration
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.engine.system.SystemEngine
import mozilla.components.concept.engine.Engine

class DankApplication : Application() {
    val components by lazy {
        Components(this)
    }
}

class Components(context: Context) {

    private val geckoEngine: Engine by lazy {
        GeckoEngine(context)
    }

    private val webViewEngine: Engine by lazy {
        SystemEngine(context)
    }

    private val realm by lazy {
        val config = RealmConfiguration.with(schema = setOf(TaskEntity::class, TabEntity::class))
        Realm.open(config)
    }

    private val realmDatabase by lazy {
        TaskRepository(realm, geckoEngine)
    }

    val taskList by lazy {
        TaskList(realmDatabase)
    }

}