package com.example.dankbrowser

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun EditText.showKeyboard() {
    post {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

val Context.application: DankApplication
    get() = applicationContext as DankApplication

val AndroidViewModel.components: Components
    get() = getApplication<DankApplication>().components

val Context.components: Components
    get() = application.components

fun AndroidViewModel.getString(@StringRes string: Int): String {
    return getApplication<DankApplication>().getString(string)
}

/**
 * Retrieves a {@link WindowInsetsControllerCompat} for the top-level window decor view.
 */
fun Window.getWindowInsetsController(): WindowInsetsControllerCompat {
    return WindowInsetsControllerCompat(this, this.decorView)
}


/**
 * Attempts to call immersive mode using the View to hide the status bar and navigation buttons.
 * @param onWindowFocusChangeListener optional callback to ensure immersive mode is stable
 * Note that the callback reference should be kept by the caller and be used for [exitImmersiveModeIfNeeded] call.
 */
fun Activity.enterToImmersiveMode(
    onWindowFocusChangeListener: ViewTreeObserver.OnWindowFocusChangeListener? = null,
) {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    window.getWindowInsetsController().apply {
        hide(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    // We need to make sure system bars do not become permanently visible after interactions with content
    // see https://github.com/mozilla-mobile/fenix/issues/20240
    onWindowFocusChangeListener?.let {
        window.decorView.viewTreeObserver?.addOnWindowFocusChangeListener(it)
    }
}

/**
 * Attempts to come out from immersive mode using the View.
 * @param onWindowFocusChangeListener optional callback to ensure immersive mode is stable
 * Note that the callback reference should be kept by the caller and be the same used for [enterToImmersiveMode] call.
 */
@Suppress("DEPRECATION")
fun Activity.exitImmersiveModeIfNeeded(
    onWindowFocusChangeListener: ViewTreeObserver.OnWindowFocusChangeListener? = null,
) {
    if (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON and window.attributes.flags == 0) {
        // We left immersive mode already.
        return
    }
    onWindowFocusChangeListener?.let {
        window.decorView.viewTreeObserver?.removeOnWindowFocusChangeListener(it)
    }
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    window.getWindowInsetsController().apply {
        show(WindowInsetsCompat.Type.systemBars())
    }
}