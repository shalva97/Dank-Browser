package com.example.dankbrowser.presentation.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.dankbrowser.DankApplication
import com.example.dankbrowser.components
import com.example.dankbrowser.domain.TaskList
import com.example.dankbrowser.domain.Url
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    private val geckoRuntime: GeckoRuntime = (application as DankApplication).components.geckoEngine
    val selectedTask = taskList.selectedTask
    val urlBar = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        if (selectedTask.selectedTab.url is Url.Empty) {
            urlBar.postValue(true)
        } else {
            selectedTask.selectedTab.loadWebsite(geckoRuntime)
            selectedTask.selectedTab.geckoSession.navigationDelegate = navigationDelegate()
            selectedTask.selectedTab.geckoSession.progressDelegate = progressDelegate()
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

    private fun progressDelegate(): GeckoSession.ProgressDelegate {
        return object : GeckoSession.ProgressDelegate {
            override fun onPageStart(session: GeckoSession, url: String) {
                super.onPageStart(session, url)
                loading.postValue(true)
            }

            override fun onPageStop(session: GeckoSession, success: Boolean) {
                super.onPageStop(session, success)
                loading.postValue(false)
            }

        }
    }

    fun changeUrl(url: String) {
        taskList.changeUrl(selectedTask.selectedTab, url, selectedTask)
    }

    fun hideUrlBar() {
        urlBar.postValue(false)
    }

}
