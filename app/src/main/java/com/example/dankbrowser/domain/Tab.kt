package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TabEntity
import io.realm.Realm
import kotlinx.coroutines.flow.MutableStateFlow
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

data class Tab(
    private var originalObject: TabEntity,
    private val realm: Realm
) {

    val url: MutableStateFlow<Url> = MutableStateFlow(Url.Empty)
    val contextId: String
    val title = MutableStateFlow<String>("")

    init {
        if (originalObject.url.isNotEmpty()) {
            url.tryEmit(Url.Website(originalObject.url))
        }
        contextId = originalObject.contextId
        title.tryEmit(originalObject.title)
    }

    val geckoSession: GeckoSession by lazy {
        GeckoSession()
    }

    fun loadWebsite(geckoRuntime: GeckoRuntime) {
        val url = url.value
        if (!geckoSession.isOpen && url is Url.Website) {
            geckoSession.open(geckoRuntime)
            geckoSession.loadUri(url.url)
        }
    }

    fun saveAndLoadUrl(url: String) {
        saveUrl(url)
        geckoSession.loadUri(url)
    }

    fun saveUrl(url: String) {
        realm.writeBlocking {
            originalObject = findLatest(originalObject)?.apply {
                this.url = url
            }!!
        }
        this.url.tryEmit(Url.Website(url))
    }

    fun saveTitle(title: String) {
        realm.writeBlocking {
            originalObject = findLatest(originalObject)?.apply {
                this.title = title
            }!!
        }
        this.title.tryEmit(title)
    }

    sealed class Url {
        object Empty : Url()
        data class Website(val url: String) : Url()
    }
}
