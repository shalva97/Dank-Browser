package com.example.dankbrowser.domain

import com.example.dankbrowser.data.TabEntity
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mozilla.components.concept.engine.Engine
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.prompt.PromptRequest

class Tab(
    private var originalObject: TabEntity,
    private val realm: Realm,
    private val engine: Engine,
) {

    val url: MutableStateFlow<Url> = MutableStateFlow(Url.Empty)
    private val contextId: String
    val title = MutableStateFlow<String>("")
    val isLoading = MutableStateFlow(false)
    val isFullscreen = MutableStateFlow(false)
    val prompts = MutableSharedFlow<PromptRequest>(extraBufferCapacity = 1)
    private var isInitialized = false
    val scope = CoroutineScope(Dispatchers.IO)

    init {
        if (originalObject.url.isNotEmpty()) {
            url.tryEmit(Url.Website(originalObject.url))
        }
        contextId = originalObject.contextId
        title.tryEmit(originalObject.title)
    }

    val geckoEngineSession: EngineSession by lazy {
        engine.createSession(contextId = contextId)
    }

    fun initialize() {
        val url = url.value
        if (!isInitialized && url is Url.Website) {
            geckoEngineSession.loadUrl(url.url)
            geckoEngineSession.register(object : EngineSession.Observer {
                override fun onLocationChange(url: String) {
                    scope.launch {
                        saveUrl(url)
                    }
                }

                override fun onProgress(progress: Int) {
                    isLoading.tryEmit(progress < 100)
                }

                override fun onTitleChange(title: String) {
                    scope.launch {
                        saveTitle(title)
                    }
                }

                override fun onFullScreenChange(enabled: Boolean) {
                    isFullscreen.tryEmit(enabled)
                }

                override fun onPromptRequest(promptRequest: PromptRequest) {
                    scope.launch {
                        prompts.emit(promptRequest)
                    }
                }
            })
            isInitialized = true
        }
    }

    suspend fun saveAndLoadUrl(url: String) {
        saveUrl(url)
        geckoEngineSession.loadUrl(url)
    }

    suspend fun saveUrl(url: String) {
        realm.write {
            originalObject = findLatest(originalObject)?.apply {
                this.url = url
            }!!
        }
        this.url.emit(Url.Website(url))
    }

    private suspend fun saveTitle(title: String) {
        realm.write {
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