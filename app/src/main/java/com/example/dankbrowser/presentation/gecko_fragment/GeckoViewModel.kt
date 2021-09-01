package com.example.dankbrowser.presentation.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dankbrowser.DankApplication
import com.example.dankbrowser.components
import com.example.dankbrowser.domain.TaskList
import com.example.dankbrowser.domain.Url
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    private val geckoRuntime: GeckoRuntime = (application as DankApplication).components.geckoEngine
    val selectedTask = taskList.getSelectedTab()
    val urlBar = MutableSharedFlow<Boolean>(1, 1)

    init {
        selectedTask.onEach { task ->
            if (task.selectedTab.url is Url.Empty) {
                urlBar.tryEmit(true)
            } else {
                task.selectedTab.loadWebsite(geckoRuntime)
                hideUrlBar()
            }

            task.selectedTab.geckoSession.navigationDelegate = navigationDelegate()
        }.launchIn(viewModelScope)
    }

    private fun navigationDelegate(): GeckoSession.NavigationDelegate {
        return object : GeckoSession.NavigationDelegate {
            override fun onLocationChange(session: GeckoSession, url: String?) {
                if (url != null) {
                    changeUrl(url)
                }
            }

        }
    }

    fun changeUrl(url: String) {
        val task = selectedTask.replayCache.first()
        taskList.changeUrl(task.selectedTab, url, task)
    }

    fun hideUrlBar() {
        urlBar.tryEmit(false)
    }

}
