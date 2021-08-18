package com.example.dankbrowser

import android.app.Application
import android.content.Context
import com.example.dankbrowser.data.TaskRepository
import com.example.dankbrowser.task_view.TaskList
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

    val realmDatabase by lazy {
        TaskRepository()
    }

    val taskList by lazy {
        TaskList(realmDatabase)
    }


}