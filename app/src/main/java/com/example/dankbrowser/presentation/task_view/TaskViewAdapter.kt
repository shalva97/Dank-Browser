package com.example.dankbrowser.presentation.task_view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.dankbrowser.databinding.NewTabRvItemBinding
import com.example.dankbrowser.databinding.TabRvItemBinding
import com.example.dankbrowser.databinding.TaskViewNameRvItemBinding
import com.example.dankbrowser.domain.Tab
import com.example.dankbrowser.domain.Task
import com.example.dankbrowser.presentation.task_view.models.ITaskListRVBindings
import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem
import com.example.dankbrowser.presentation.task_view.models.rv_types.RVViewTypes
import kotlinx.coroutines.flow.MutableSharedFlow

class TaskViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var taskList: ITaskListRVBindings
    val tabTapped = MutableSharedFlow<RVItem.TabUI>(0, 1)
    val taskTapped = MutableSharedFlow<RVItem.TaskUI>(0, 1)
    val deleteTaskTapped = MutableSharedFlow<RVItem.TaskUI>(0, 1)
    val newTabTapped = MutableSharedFlow<Task>(0, 1)
    val deleteTabTapped = MutableSharedFlow<Pair<Task, Tab>>(0, 1)
    val renameTaskTapped = MutableSharedFlow<Task>(0, 1)
    val changeContextTapped = MutableSharedFlow<Task>(0, 1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RVViewTypes.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val itemAtIndex = taskList.getItemAtIndex(position)) {
            is RVItem.NewTabButton -> {
                NewTabRvItemBinding.bind(holder.itemView).apply {
                    root.setOnClickListener {
                        newTabTapped.tryEmit(itemAtIndex.task)
                    }
                }
            }
            is RVItem.TabUI -> {
                TabRvItemBinding.bind(holder.itemView).apply {
                    titleTV.text = itemAtIndex.name
                    root.setOnClickListener {
                        tabTapped.tryEmit(itemAtIndex)
                    }
                    closeIB.setOnClickListener {
                        deleteTabTapped.tryEmit(itemAtIndex.task to itemAtIndex.originalObject)
                    }
                }
            }
            is RVItem.TaskUI -> {
                TaskViewNameRvItemBinding.bind(holder.itemView).apply {
                    titleTV.text = itemAtIndex.name
                    root.setOnClickListener {
                        taskTapped.tryEmit(itemAtIndex)
                    }

                    overflowMenuIV.setOnClickListener {
                        showOverflowMenu(holder.itemView.context, it, itemAtIndex)
                    }

                }
            }
        }
    }

    private fun showOverflowMenu(context: Context, anchor: View, itemAtIndex: RVItem.TaskUI) {
        val menu = PopupMenu(context, anchor, Gravity.AXIS_PULL_AFTER)

        menu.menu.apply {
            add("Rename").setOnMenuItemClickListener {
                renameTaskTapped.tryEmit(itemAtIndex.originalObject)
                true
            }
            add("change context").setOnMenuItemClickListener {
                changeContextTapped.tryEmit(itemAtIndex.originalObject)
                true
            }

            add("delete").setOnMenuItemClickListener {
                deleteTaskTapped.tryEmit(itemAtIndex)
                true
            }
        }

        menu.show()
    }

    override fun getItemCount(): Int {
        return taskList.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return taskList.getItemAtIndex(position).getViewType()
    }


}