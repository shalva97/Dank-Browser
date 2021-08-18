package com.example.dankbrowser.task_view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dankbrowser.databinding.NewTabRvItemBinding
import com.example.dankbrowser.databinding.TabRvItemBinding
import com.example.dankbrowser.databinding.TaskViewNameRvItemBinding
import com.example.dankbrowser.task_view.models.ITaskListRVBindings
import com.example.dankbrowser.task_view.models.Task
import com.example.dankbrowser.task_view.models.rv_types.RVItem
import com.example.dankbrowser.task_view.models.rv_types.RVViewTypes
import kotlinx.coroutines.flow.MutableSharedFlow

class TaskViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var taskList: ITaskListRVBindings
    val tabTapped = MutableSharedFlow<RVItem.TabUI>(0, 1)
    val taskTapped = MutableSharedFlow<RVItem.TaskUI>(0, 1)
    val deleteTaskTapped = MutableSharedFlow<RVItem.TaskUI>(0, 1)
    val newTabTapped = MutableSharedFlow<Task>(0, 1)

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
                }
            }
            is RVItem.TaskUI -> {
                TaskViewNameRvItemBinding.bind(holder.itemView).apply {
                    titleTV.text = itemAtIndex.name
                    root.setOnClickListener {
                        taskTapped.tryEmit(itemAtIndex)
                    }

                    deleteIV.setOnClickListener {
                        deleteTaskTapped.tryEmit(itemAtIndex)
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return taskList.getItemAtIndex(position).getViewType()
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class TabViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class NewTabBTNViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class NetTaskBTNViewHolder(view: View) : RecyclerView.ViewHolder(view)


}