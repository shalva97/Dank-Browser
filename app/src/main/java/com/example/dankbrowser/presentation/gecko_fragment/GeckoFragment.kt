package com.example.dankbrowser.presentation.gecko_fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentGeckoBinding
import com.example.dankbrowser.enterToImmersiveMode
import com.example.dankbrowser.exitImmersiveModeIfNeeded
import com.example.dankbrowser.hideKeyboard
import com.example.dankbrowser.presentation.gecko_fragment.prompts.ChoiceDialogFragment
import com.example.dankbrowser.presentation.gecko_fragment.prompts.ChoiceDialogFragment.Companion.SINGLE_CHOICE_DIALOG_TYPE
import com.example.dankbrowser.presentation.gecko_fragment.prompts.Prompter
import mozilla.components.concept.engine.prompt.Choice
import mozilla.components.concept.engine.prompt.PromptRequest
import mozilla.components.support.base.dialog.DeniedPermissionDialogFragment.Companion.FRAGMENT_TAG
import java.io.InvalidClassException

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

        pageTitleTV.setOnClickListener {
            findNavController().navigate(R.id.action_geckoFragment_to_smallTaskListFragment)
        }

        browserGV.render(viewModel.selectedTab.geckoEngineSession)

        viewModel.urlBar.observe(viewLifecycleOwner) {
            urlBarEWC.isVisible = it
            hideKeyboard()
        }

        viewModel.prompts.observe(viewLifecycleOwner) {
            it?.let { promptRequest: PromptRequest ->
                val dialog = when (promptRequest) {
                    is PromptRequest.SingleChoice -> {
                        ChoiceDialogFragment.newInstance(
                            promptRequest.choices,
                            promptRequest.uid,
                            true,
                            SINGLE_CHOICE_DIALOG_TYPE
                        )
                    }

                    else -> throw InvalidClassException("asdf")
                }

                dialog.feature = object : Prompter {

                    override fun onConfirm(promptRequestUID: String, value: Any?) {
                        if (promptRequest is PromptRequest.SingleChoice) {
                            promptRequest.onConfirm.invoke(value as Choice)
                        }
                    }
                }

                dialog.show(requireFragmentManager(), FRAGMENT_TAG)
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
                requireActivity().enterToImmersiveMode()
            } else {
                requireActivity().exitImmersiveModeIfNeeded()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.browserGoBack()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().exitImmersiveModeIfNeeded()
    }
}
