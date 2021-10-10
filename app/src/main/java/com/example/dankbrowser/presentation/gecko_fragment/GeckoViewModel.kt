package com.example.dankbrowser.presentation.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dankbrowser.DankApplication
import com.example.dankbrowser.components
import com.example.dankbrowser.domain.TaskList
import com.example.dankbrowser.domain.Url
import kotlinx.coroutines.flow.MutableSharedFlow
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    private val geckoRuntime: GeckoRuntime = (application as DankApplication).components.geckoEngine
    val selectedTask = taskList.selectedTask
    val urlBar = MutableSharedFlow<Boolean>(1, 1)

    init {
        if (selectedTask.selectedTab.url is Url.Empty) {
            urlBar.tryEmit(true)
        } else {
            selectedTask.selectedTab.loadWebsite(geckoRuntime)
            selectedTask.selectedTab.geckoSession.navigationDelegate = navigationDelegate()
            hideUrlBar()
        }
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
        taskList.changeUrl(selectedTask.selectedTab, url, selectedTask)
    }

    fun hideUrlBar() {
        urlBar.tryEmit(false)
    }

}
