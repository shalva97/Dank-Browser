package com.example.dankbrowser.task_view.models

import com.example.dankbrowser.data.TabEntity

data class Tab(
    val url: String,
    val contextId: String,
    val title: String,
    val originalObject: TabEntity
)