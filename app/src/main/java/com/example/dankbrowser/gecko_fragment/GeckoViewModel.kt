package com.example.dankbrowser.gecko_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.dankbrowser.components
import com.example.dankbrowser.task_view.TaskList

class GeckoViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val taskList: TaskList = components.taskList
    val loadUrlAction = taskList.getSelectedTab()

}