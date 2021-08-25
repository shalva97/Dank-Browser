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
    lateinit var geckoSession: GeckoSession

    fun isInitialized(): Boolean {
        return ::geckoSession.isInitialized
    }

    fun getSession(): GeckoSession {
        if (!::geckoSession.isInitialized) {
            geckoSession = GeckoSession()
        }
        return geckoSession
    }

    fun loadWebsite(geckoRuntime: GeckoRuntime) {
        getSession()
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
