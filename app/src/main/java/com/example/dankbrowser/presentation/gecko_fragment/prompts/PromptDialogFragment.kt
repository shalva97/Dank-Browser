/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.example.dankbrowser.presentation.gecko_fragment.prompts

import androidx.fragment.app.DialogFragment
import mozilla.components.concept.engine.prompt.PromptRequest

internal const val KEY_TITLE = "KEY_TITLE"
internal const val KEY_MESSAGE = "KEY_MESSAGE"
internal const val KEY_PROMPT_UID = "KEY_PROMPT_UID"
internal const val KEY_SHOULD_DISMISS_ON_LOAD = "KEY_SHOULD_DISMISS_ON_LOAD"

/**
 * An abstract representation for all different types of prompt dialogs.
 * for handling [PromptFeature] dialogs.
 */
internal abstract class PromptDialogFragment : DialogFragment() {
    var feature: Prompter? = null

    internal val promptRequestUID: String by lazy {
        requireNotNull(arguments).getString(
            KEY_PROMPT_UID
        )!!
    }

    val safeArguments get() = requireNotNull(arguments)
}

internal interface Prompter {
    /**
     * Invoked when the user confirms the action on the dialog. This consumes the [PromptRequest] indicated
     * by [promptRequestUID] from the session indicated by [sessionId].
     *
     * @param sessionId that requested to show the dialog.
     * @param promptRequestUID id of the [PromptRequest] for which this dialog was shown.
     * @param value an optional value provided by the dialog as a result of confirming the action.
     */
    fun onConfirm(promptRequestUID: String, value: Any?)

}
