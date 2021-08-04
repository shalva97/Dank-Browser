package com.example.dankbrowser.task_view.models

import com.example.dankbrowser.task_view.models.rv_types.RVItem

interface ITaskListRVBindings {
    fun getItemCount(): Int
    fun getItemAtIndex(index: Int): RVItem
    var list: MutableList<Task>
    fun onDataChanged()
    fun addOnDataChangedCallback(cb: () -> Unit)
}