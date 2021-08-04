package com.example.dankbrowser.task_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentTaskViewBinding
import com.example.dankbrowser.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskViewFragment : Fragment(R.layout.fragment_task_view) {

    private val viewModel: TaskViewViewModel by viewModels()
    private val adapter = TaskViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(FragmentTaskViewBinding.bind(view)) {
            adapter.taskList = viewModel.getRVData()
            taskViewListRV.adapter = adapter
            adapter.taskList.addOnDataChangedCallback {
                adapter.notifyDataSetChanged()
            }

            addTaskBTN.setOnClickListener {
                showAddTaskView()
            }

            addTaskATV.onCancel {
                hideAddTaskView()
            }

            addTaskATV.onAddTask {
                viewModel.addTask(it)
                hideAddTaskView()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    adapter.deleteTaskTapped.onEach {
                        viewModel.deleteTask(it.originalObject)
                    }.launchIn(this)

                    adapter.tabTapped.onEach {
                        viewModel.switchToTab(it.originalObject) // TODO
                    }.launchIn(this)

                    viewModel.navigation.onEach {
                        findNavController().navigate(it)
                    }.launchIn(this)
                }
            }
        }
    }

    private fun FragmentTaskViewBinding.showAddTaskView() {
        addTaskBTN.isVisible = false
        addTaskATV.isVisible = true
        hideKeyboard()
    }

    private fun FragmentTaskViewBinding.hideAddTaskView() {
        addTaskBTN.isVisible = true
        addTaskATV.isVisible = false
        hideKeyboard()
    }
}