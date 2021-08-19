package com.example.dankbrowser.presentation.task_view.models

import com.example.dankbrowser.domain.Task

interface ITaskList {
    var list: MutableList<Task>
}