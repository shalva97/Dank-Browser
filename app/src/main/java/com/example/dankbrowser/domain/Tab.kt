package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TabEntity
import io.realm.Realm
import kotlinx.coroutines.flow.MutableStateFlow
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings

class Tab(
    private var originalObject: TabEntity,
    private val realm: Realm,
    private val geckoRuntime: GeckoRuntime
) {

    val url: MutableStateFlow<Url> = MutableStateFlow(Url.Empty)
    val contextId: String
    val title = MutableStateFlow<String>("")
    val isLoading = MutableStateFlow(false)
    val isFullscreen = MutableStateFlow(false)

    init {
        if (originalObject.url.isNotEmpty()) {
            url.tryEmit(Url.Website(originalObject.url))
        }
        contextId = originalObject.contextId
        title.tryEmit(originalObject.title)
    }

    val geckoSession: GeckoSession by lazy {
        val settings = GeckoSessionSettings.Builder().contextId(contextId).build()
        GeckoSession(settings)
    }

    fun initialize() {
        val url = url.value
        if (!geckoSession.isOpen && url is Url.Website) {
            geckoSession.open(geckoRuntime)
            geckoSession.loadUri(url.url)
            geckoSession.navigationDelegate = navigationDelegate(::saveUrl)
            geckoSession.progressDelegate = progressDelegate(isLoading::tryEmit)
            geckoSession.contentDelegate = contentDelegate(::saveTitle, isFullscreen::tryEmit)
            // TODO add onChoicePrompt
//            geckoSession.promptDelegate =  object : GeckoSession.PromptDelegate {
//                override fun onChoicePrompt(
//                    session: GeckoSession,
//                    prompt: GeckoSession.PromptDelegate.ChoicePrompt
//                ): GeckoResult<GeckoSession.PromptDelegate.PromptResponse>? {
//                    return super.onChoicePrompt(session, prompt)
//                }
//            }
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

    private fun saveTitle(title: String) {
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

private fun navigationDelegate(saveUrl: (String) -> Unit): GeckoSession.NavigationDelegate {
    return object : GeckoSession.NavigationDelegate {
        override fun onLocationChange(session: GeckoSession, url: String?) {
            if (url != null) {
                saveUrl(url)
            }
        }

    }
}

private fun contentDelegate(
    saveTitle: (String) -> Unit,
    onFullScreen: (Boolean) -> Unit
): GeckoSession.ContentDelegate {
    return object : GeckoSession.ContentDelegate {
        override fun onTitleChange(session: GeckoSession, title: String?) {
            title?.let {
                saveTitle(it)
            }
        }

        override fun onFullScreen(session: GeckoSession, fullScreen: Boolean) {
            onFullScreen(fullScreen)
        }
    }
}

private fun progressDelegate(isLoading: (Boolean) -> Unit): GeckoSession.ProgressDelegate {
    return object : GeckoSession.ProgressDelegate {
        override fun onPageStart(session: GeckoSession, url: String) {
            isLoading(true)
        }

        override fun onPageStop(session: GeckoSession, success: Boolean) {
            isLoading(false)
        }

    }
}