package com.example.dankbrowser.presentation.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dankbrowser.components
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.TaskList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    val selectedTab = taskList.selectedTask.selectedTab
    val urlBar = MutableLiveData<Boolean>()
    val loading = selectedTab.isLoading.asLiveData(viewModelScope.coroutineContext)
    val pageTitle = selectedTab.title.asLiveData(viewModelScope.coroutineContext)
    val isFullscreen = selectedTab.isFullscreen.asLiveData(viewModelScope.coroutineContext)
    val prompts = selectedTab.prompts.asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            selectedTab.url.onEach {
                if (selectedTab.url.value is Tab.Url.Empty) {
                    urlBar.postValue(true)
                } else {
                    selectedTab.initialize()
                    hideUrlBar()
                }
            }.launchIn(this)
        }
    }

    fun loadUrl(url: String) {
        viewModelScope.launch {
            selectedTab.saveAndLoadUrl(url)
        }
    }

    fun browserGoBack() {
        selectedTab.geckoEngineSession.goBack()
    }

    fun hideUrlBar() {
        urlBar.postValue(false)
    }

}
