package com.pioneer.ete.finalproject2022.ext

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * This method will be used when we want to hide keyboard of view
 */
@SuppressLint("ClickableViewAccessibility")
fun View.touchHideKeyboard(isClearFocus: Boolean = true) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (this !is EditText || !this.isFocusable()) {
        this.setOnTouchListener { _, _ ->
            this.hideKeyboard()
            if (isClearFocus) {
                this.clearFocus()
            }
            false
        }
    }
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            this.getChildAt(i)
            val innerView = this.getChildAt(i)
            innerView.touchHideKeyboard()
        }
    }
}

/**
 * This method will be used when we want to hide keyboard
 */
fun View.hideKeyboard() =
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(this@hideKeyboard.windowToken, 0)
    }
