package com.example.dankbrowser.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dankbrowser.DankApplication
import com.example.dankbrowser.components
import com.example.dankbrowser.task_view.TaskList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.mozilla.geckoview.GeckoRuntime

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    private val geckoRuntime: GeckoRuntime = (application as DankApplication).components.geckoEngine
    val selectedTab = taskList.getSelectedTab()

    init {
        selectedTab.onEach {
            it.loadWebsite(geckoRuntime)
        }.launchIn(viewModelScope)
    }

}