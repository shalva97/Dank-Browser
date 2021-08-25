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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.mozilla.geckoview.GeckoView

class GeckoFragment : Fragment(R.layout.fragment_gecko) {

    private val viewModel: GeckoViewModel by viewModels()
    private var browserView: GeckoView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentGeckoBinding.bind(view).init()
    }

    private fun FragmentGeckoBinding.init() {
        browserView = browserGV

        urlBarEWC.onPositive {
            viewModel.changeUrl(it)
        }

        urlBarEWC.onCancel {
            viewModel.hideUrlBar()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.selectedTask.onEach {
                    if (browserGV.session != it.selectedTab.getSession()) {
                        browserGV.setSession(it.selectedTab.getSession())
                    }
                }.launchIn(this)

                viewModel.urlBar.onEach {
                    urlBarEWC.isVisible = it
                }.launchIn(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        browserView?.releaseSession()
    }
}