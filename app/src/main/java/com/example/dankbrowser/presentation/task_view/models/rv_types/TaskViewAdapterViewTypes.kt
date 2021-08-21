package com.example.dankbrowser.presentation.task_view.models.rv_types

import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task

sealed class RVItem(private val type: RVViewTypes) {

    class TaskUI(val name: String, val originalObject: Task) : RVItem(RVViewTypes.TASK_TITLE)
    class TabUI(val name: String, val originalObject: Tab, val task: Task) :
        RVItem(RVViewTypes.TAB)

    class NewTabButton(val task: Task) : RVItem(RVViewTypes.NEW_TAB_BTN)

    fun getViewType(): Int {
        return type.viewType
    }
}