package com.example.dankbrowser.task_view.models.rv_types

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dankbrowser.R
import com.example.dankbrowser.task_view.TaskViewAdapter
import java.security.InvalidParameterException

enum class RVViewTypes(val viewType: Int) {
    TASK_TITLE(0), TAB(1), NEW_TAB_BTN(2);

    companion object {
        fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)

            return when (viewType) {
                RVViewTypes.TASK_TITLE.viewType -> {
                    val layout = view.inflate(R.layout.task_view_name_rv_item, parent, false)
                    TaskViewAdapter.TaskViewHolder(layout)
                }
                RVViewTypes.TAB.viewType -> {
                    val layout = view.inflate(R.layout.tab_rv_item, parent, false)
                    TaskViewAdapter.TabViewHolder(layout)
                }
                RVViewTypes.NEW_TAB_BTN.viewType -> {
                    val layout = view.inflate(R.layout.new_tab_rv_item, parent, false)
                    TaskViewAdapter.NewTabBTNViewHolder(layout)
                }
                else -> {
                    throw InvalidParameterException()
                }
            }
        }
    }
}