package com.example.dankbrowser.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dankbrowser.task_view.TaskList
import com.example.dankbrowser.task_view.models.Tab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GeckoViewModel @Inject constructor(
    application: Application,
    private val taskList: TaskList
) : AndroidViewModel(application) {

    val loadUrlAction = taskList.getSelectedTab()


}