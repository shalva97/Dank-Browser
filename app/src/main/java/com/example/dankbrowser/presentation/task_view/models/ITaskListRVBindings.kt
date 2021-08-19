package com.example.dankbrowser.presentation.task_view.models

import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem

interface ITaskListRVBindings : ITaskList {
    fun getItemCount(): Int
    fun getItemAtIndex(index: Int): RVItem
    fun onDataChanged()
    fun addOnDataChangedCallback(cb: () -> Unit)
}