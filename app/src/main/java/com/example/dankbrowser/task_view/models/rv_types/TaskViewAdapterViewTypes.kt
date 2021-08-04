package com.example.dankbrowser.task_view.models.rv_types

import com.example.dankbrowser.task_view.models.Tab
import com.example.dankbrowser.task_view.models.Task

sealed class RVItem(private val type: RVViewTypes) {

    class TaskUI(val name: String, val originalObject: Task): RVItem(RVViewTypes.TASK_TITLE)
    class TabUI(val name: String, url: String, val originalObject: Tab): RVItem(RVViewTypes.TAB)
    object NewTabButton: RVItem(RVViewTypes.NEW_TAB_BTN)
    object NewTaskButton: RVItem(RVViewTypes.NEW_TASK_BTN)

    fun getViewType(): Int {
        return type.viewType
    }
}