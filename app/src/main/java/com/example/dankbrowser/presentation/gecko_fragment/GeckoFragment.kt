package com.example.dankbrowser.presentation.gecko_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentGeckoBinding
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

        viewModel.selectedTask.observe(viewLifecycleOwner) {
            if (browserGV.session != it.selectedTab.getSession()) {
                browserGV.setSession(it.selectedTab.getSession())
            }
        }

        viewModel.urlBar.observe(viewLifecycleOwner) {
            urlBarEWC.isVisible = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        browserView?.releaseSession()
    }
}