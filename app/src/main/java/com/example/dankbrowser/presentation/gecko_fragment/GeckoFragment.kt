package com.example.dankbrowser.presentation.gecko_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentGeckoBinding

class GeckoFragment : Fragment(R.layout.fragment_gecko) {

    private val viewModel: GeckoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentGeckoBinding.bind(view).init()
    }

    private fun FragmentGeckoBinding.init() {

        urlBarEWC.onPositive {
            viewModel.changeUrl(it)
        }

        urlBarEWC.onCancel {
            viewModel.hideUrlBar()
        }

        if (browserGV.session != viewModel.selectedTask.selectedTab.geckoSession) {
            browserGV.setSession(viewModel.selectedTask.selectedTab.geckoSession)
        }

        viewModel.urlBar.observe(viewLifecycleOwner) {
            urlBarEWC.isVisible = it
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                browserGV.releaseSession()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            loader.isVisible = it
        }
    }
}