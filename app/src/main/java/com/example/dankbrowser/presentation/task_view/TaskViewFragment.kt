package com.example.dankbrowser.presentation.task_view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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

        adapter.taskList = viewModel.getTaskListDataForRV()
        taskViewListRV.adapter = adapter
        adapter.taskList.addOnDataChangedCallback {
            adapter.notifyDataSetChanged()
        }

        addTaskBTN.setOnClickListener {
            showAddTaskView()
        }

        addTaskETFWC.onCancel {
            hideAddTaskView()
        }

        addTaskETFWC.onPositive {
            viewModel.addTask(it)
            hideAddTaskView()
        }

        changeContextETFWC.changeHint(getString(R.string.context_name))

        changeContextETFWC.onCancel {
            hideChangeContextField()
        }
        changeContextETFWC.onPositive {
            viewModel.changeContext(it)
            hideChangeContextField()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.deleteTaskTapped.onEach {
                    showDeleteDialog(requireContext()) {
                        viewModel.deleteTask(it.originalObject)
                    }
                }.launchIn(this)

                adapter.tabTapped.onEach {
                    viewModel.switchToTab(it.originalObject, it.task)
                }.launchIn(this)

                adapter.deleteTabTapped.onEach {
                    showDeleteDialog(requireContext()) {
                        viewModel.deleteTab(it)
                    }
                }.launchIn(this)

                adapter.newTabTapped.onEach {
                    viewModel.createNewTab(it)
                }.launchIn(this)

                adapter.changeContextTapped.onEach {
                    showChangeContextField()
                }.launchIn(this)

                viewModel.navigation.onEach {
                    findNavController().navigate(it)
                }.launchIn(this)

                adapter.renameTaskTapped.onEach {
                    viewModel.renameTask(it, "somethingn else")
                }.launchIn(this)
            }
        }
    }

    private fun showDeleteDialog(context: Context, cb: () -> Unit) {
        AlertDialog.Builder(context).apply {
            setMessage(R.string.delete_task_dialog_title) // TODO pass tab/task name argument
                .setPositiveButton(R.string.delete) { dialog, id ->
                    cb.invoke()
                }.setNegativeButton(R.string.common_cancel) { dialog, id -> }
        }.create().show()
    }

    private fun FragmentTaskViewBinding.showAddTaskView() {
        addTaskBTN.isVisible = false
        addTaskETFWC.isVisible = true
        addTaskETFWC.showKeyboard()
    }

    private fun FragmentTaskViewBinding.hideAddTaskView() {
        addTaskBTN.isVisible = true
        addTaskETFWC.isVisible = false
        hideKeyboard()
    }

    private fun FragmentTaskViewBinding.hideChangeContextField() {
        addTaskBTN.isVisible = true
        changeContextETFWC.isVisible = false
        hideKeyboard()
    }

    private fun FragmentTaskViewBinding.showChangeContextField() {
        addTaskBTN.isVisible = false
        changeContextETFWC.isVisible = true
        changeContextETFWC.showKeyboard()
    }
}