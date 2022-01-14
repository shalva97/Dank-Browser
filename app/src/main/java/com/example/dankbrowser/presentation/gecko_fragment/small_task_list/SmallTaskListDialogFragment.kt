package com.example.dankbrowser.presentation.gecko_fragment.small_task_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentSmallTaskListBinding
import com.example.dankbrowser.presentation.task_view.models.rv_types.RVItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SmallTaskListDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SmallTaskListFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentSmallTaskListBinding.inflate(LayoutInflater.from(context)).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentSmallTaskListBinding.bind(view).init()
    }

    private fun FragmentSmallTaskListBinding.init() {
        backIB.setOnClickListener {
            viewModel.goBack()
        }

        goForwardIB.setOnClickListener {
            viewModel.goForward()
        }

        reloadIB.setOnClickListener {
            viewModel.reload()
        }

        list.adapter = SmallTaskFragmentAdapter(viewLifecycleOwner.lifecycleScope).apply {
            submitList(viewModel.tabs.map { RVItem.TabUI(it.title, it, viewModel.selectedTask) })
        }

        goToTaskViewIB.setOnClickListener {
            findNavController().navigate(R.id.action_smallTaskListFragment_to_taskView)
        }

        viewModel.pageTitle.observe(viewLifecycleOwner) {
            websiteTitleTV.text = it
        }

        viewModel.url.observe(viewLifecycleOwner) {
            websiteUrlTV.text = it
        }

        taskNameTV.text = viewModel.taskName
    }
}
