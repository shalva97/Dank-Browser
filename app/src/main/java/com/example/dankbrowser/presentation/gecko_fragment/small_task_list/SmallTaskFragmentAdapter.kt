package com.example.dankbrowser.presentation.gecko_fragment.small_task_list

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.TabRvItemBinding
import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import splitties.views.inflate

class SmallTaskFragmentAdapter(private val lifecycleScope: LifecycleCoroutineScope) :
    ListAdapter<RVItem.TabUI, RecyclerView.ViewHolder>(diffCallback) {

    val tabTapped = MutableSharedFlow<RVItem.TabUI>(0, 1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout: View = parent.inflate(R.layout.tab_rv_item, false)
        return object : RecyclerView.ViewHolder(layout) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TabRvItemBinding.bind(holder.itemView).apply {

            getItem(position).name.onEach {
                titleTV.text = it
            }.launchIn(lifecycleScope)

            root.setOnClickListener {
                tabTapped.tryEmit(getItem(position))
            }
//            closeIB.setOnClickListener {
//                deleteTabTapped.tryEmit(itemAtIndex.task to itemAtIndex.originalObject)
//            } // TODO
        }

    }
}

val diffCallback = object :
    DiffUtil.ItemCallback<RVItem.TabUI?>() {
    override fun areItemsTheSame(oldItem: RVItem.TabUI, newItem: RVItem.TabUI): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RVItem.TabUI, newItem: RVItem.TabUI): Boolean {
        return oldItem == newItem
    }
}
