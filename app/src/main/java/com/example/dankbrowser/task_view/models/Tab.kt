package com.example.dankbrowser.task_view.models

data class Tab(val url: String, val contextId: String, val title: String) {

    companion object {
        fun empty() : Tab {
            return Tab("epty url", "default", "New Tab")
        }
    }
}