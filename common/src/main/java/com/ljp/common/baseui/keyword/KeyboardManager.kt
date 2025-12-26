package com.ljp.common.baseui.keyword

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.ref.WeakReference

object KeyboardManager {
    fun showKeyboard(view: View?) {
        view?.let {
            it.requestFocus()
            val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val window = (it.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window,it).show(WindowInsetsCompat.Type.ime())
            }else {
                imm?.showSoftInput(it,InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
    fun hideKeyboard(view:View?) {
        val weakRef = WeakReference(view)
        weakRef.get()?.let {v->
            val window = (v.context as? android.app.Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, v).hide(WindowInsetsCompat.Type.ime())
            } else {
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }
    @Composable
    fun keywordAsState() : State<Dp> {
        val heightState = remember { mutableStateOf(0.dp) }
        val view = LocalView.current
        val density = LocalDensity.current
        DisposableEffect(view) {
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                val rootViewInsets = ViewCompat.getRootWindowInsets(view)
                val isKeyboardVisible = rootViewInsets?.isVisible(
                    WindowInsetsCompat.Type.ime()) ?: false
                val height = if (isKeyboardVisible) {
                    val immHeight = rootViewInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    with(density) { immHeight.toDp() }
                }else {
                    0.dp
                }
                heightState.value = height
            }

            onDispose { view.viewTreeObserver.removeOnGlobalLayoutListener(listener) }
        }
        return heightState
    }
}