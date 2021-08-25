package com.example.dankbrowser.presentation.task_view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentTaskViewBinding
import com.example.dankbrowser.hideKeyboard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TaskViewFragment : Fragment(R.layout.fragment_task_view) {

    private val viewModel: TaskViewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentTaskViewBinding.bind(view).init()
    }

    private fun FragmentTaskViewBinding.init() {
        val adapter = TaskViewAdapter()

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

        addTaskATV.onPositive {
            viewModel.addTask(it)
            hideAddTaskView()
        }

        setupRVListenersAndNavigation(adapter)
    }

    private fun setupRVListenersAndNavigation(adapter: TaskViewAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.deleteTaskTapped.onEach {
                    viewModel.deleteTask(it.originalObject)
                }.launchIn(this)

                adapter.tabTapped.onEach {
                    viewModel.switchToTab(it.originalObject, it.task)
                }.launchIn(this)

                adapter.deleteTabTapped.onEach {
                    viewModel.deleteTab(it)
                }.launchIn(this)

                adapter.newTabTapped.onEach {
                    viewModel.createNewTab(it)
                }.launchIn(this)

                viewModel.navigation.onEach {
                    findNavController().navigate(it)
                }.launchIn(this)
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