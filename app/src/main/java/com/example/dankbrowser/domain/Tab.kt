package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TabEntity
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

data class Tab(
    val url: Url,
    val contextId: String,
    val title: String,
    val originalObject: TabEntity
) {
    val geckoSession: GeckoSession by lazy {
        GeckoSession()
    }

    fun loadWebsite(geckoRuntime: GeckoRuntime) {
        if (!geckoSession.isOpen && url is Url.Website) {
            geckoSession.loadUri(url.url)
            geckoSession.open(geckoRuntime)
        }
    }
}

sealed class Url {
    object Empty : Url()
    data class Website(val url: String) : Url()
}
