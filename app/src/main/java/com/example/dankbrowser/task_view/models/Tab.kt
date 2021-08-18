package com.example.dankbrowser.task_view.models

import com.example.dankbrowser.data.TabEntity
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

data class Tab(
    val url: String,
    val contextId: String,
    val title: String,
    val originalObject: TabEntity
) {
    val geckoSession: GeckoSession by lazy {
        GeckoSession()
    }

    fun loadWebsite(geckoRuntime: GeckoRuntime) {
        if (!geckoSession.isOpen) {
            geckoSession.loadUri(url)
            geckoSession.open(geckoRuntime)
        }
    }
}