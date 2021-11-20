package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TabEntity
import io.realm.Realm
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

data class Tab(
    private var originalObject: TabEntity,
    private val realm: Realm
) {

    val url: Url
    val contextId: String
    val title: String

    init {
        url = if (originalObject.url.isEmpty()) {
            Url.Empty
        } else {
            Url.Website(originalObject.url)
        }
        contextId = originalObject.contextId
        title = originalObject.title
    }

    val geckoSession: GeckoSession by lazy {
        GeckoSession()
    }

    fun loadWebsite(geckoRuntime: GeckoRuntime) {
        if (!geckoSession.isOpen && url is Url.Website) {
            geckoSession.open(geckoRuntime)
            geckoSession.loadUri("https://youtube.com")
        }
    }

    fun changeUrl(url: String) {
        // save new url to db
        // tell  everyone that url has been changed
        // load new url
        realm.writeBlocking {
            originalObject = findLatest(originalObject)?.apply {
                this.url = url
            }!!
        }

        geckoSession.loadUri(url)
    }

    sealed class Url {
        object Empty : Url()
        data class Website(val url: String) : Url()
    }
}
