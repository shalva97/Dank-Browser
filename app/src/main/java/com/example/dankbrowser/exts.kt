package com.example.dankbrowser

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
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