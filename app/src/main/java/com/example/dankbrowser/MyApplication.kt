package com.example.dankbrowser

import android.app.Application
import android.content.Context
import com.example.dankbrowser.data.TabEntity
import com.example.dankbrowser.data.TaskEntity
import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.domain.TaskList
import io.realm.Realm
import io.realm.RealmConfiguration
import org.mozilla.geckoview.GeckoRuntime

class DankApplication : Application() {
    val components by lazy {
        Components(this)
    }
}

class Components(context: Context) {

    val geckoEngine by lazy {
        GeckoRuntime.create(context)
    }

    private val realm by lazy {
        val config = RealmConfiguration(schema = setOf(TaskEntity::class, TabEntity::class))
        Realm(config)
    }

    private val realmDatabase by lazy {
        TaskRepository(realm)
    }

    val taskList by lazy {
        TaskList(realmDatabase)
    }

}