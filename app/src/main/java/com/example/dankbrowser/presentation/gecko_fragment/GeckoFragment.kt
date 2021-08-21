package com.example.dankbrowser.presentation.gecko_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentGeckoBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GeckoFragment : Fragment(R.layout.fragment_gecko) {

    private val viewModel: GeckoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGeckoBinding.bind(view)



        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.selectedTab.collect {
                    binding.browserGV.setSession(it.geckoSession)
                }
            }
        }

    }
}