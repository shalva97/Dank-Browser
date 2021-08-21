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

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    private val geckoRuntime: GeckoRuntime = (application as DankApplication).components.geckoEngine
    val selectedTab = taskList.getSelectedTab()
    val urlBar = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    init {
        selectedTab.onEach {
            if (it.url is Url.Empty) {
                urlBar.tryEmit(true)
            } else {
                it.loadWebsite(geckoRuntime)
            }
        }.launchIn(viewModelScope)
    }

}