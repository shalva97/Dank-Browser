package com.example.dankbrowser.presentation.gecko_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentGeckoBinding
import com.example.dankbrowser.hideKeyboard

class GeckoFragment : Fragment(R.layout.fragment_gecko) {

    private val viewModel: GeckoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentGeckoBinding.bind(view).init()
    }

    private fun FragmentGeckoBinding.init() {

        urlBarEWC.onPositive {
            viewModel.loadUrl(it)
        }

        urlBarEWC.onCancel {
            viewModel.hideUrlBar()
        }

        if (browserGV.session != viewModel.selectedTab.geckoSession) {
            browserGV.setSession(viewModel.selectedTab.geckoSession)
        }

        viewModel.urlBar.observe(viewLifecycleOwner) {
            urlBarEWC.isVisible = it
            hideKeyboard()
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                browserGV.releaseSession()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            loader.isVisible = it
        }

        viewModel.pageTitle.observe(viewLifecycleOwner) {
            pageTitleTV.text = it
        }

        viewModel.isFullscreen.observe(viewLifecycleOwner) { isFullscreen ->
            fullscreenGroup.isVisible = !isFullscreen
            if (isFullscreen) {
                WindowInsetsControllerCompat(activity?.window!!, root).apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                WindowInsetsControllerCompat(activity?.window!!, root)
                    .show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}