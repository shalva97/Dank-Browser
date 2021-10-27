package com.example.dankbrowser.presentation.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
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
    val selectedTask = taskList.getSelectedTab().asLiveData()
    val urlBar = MutableLiveData<Boolean>()

    init {
        selectedTask.observeForever { task ->
            if (task.selectedTab.url is Url.Empty) {
                urlBar.postValue(true)
            } else {
                task.selectedTab.loadWebsite(geckoRuntime)
                hideUrlBar()
            }

            task.selectedTab.geckoSession.navigationDelegate = navigationDelegate()
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
        val task = selectedTask.value
        taskList.changeUrl(task!!.selectedTab, url, task)
    }

    fun hideUrlBar() {
        urlBar.postValue(false)
    }

}
