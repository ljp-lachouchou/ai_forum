package ai.ljp.navigation

import androidx.navigation3.runtime.NavKey
import kotlin.compareTo
import kotlin.text.clear

class Navigator(val state: NavigationState) {
    fun navigate(key : NavKey) {
        when(key) {
            state.currentTopKey -> clearSubStack()
            in state.topKeys -> goToTop(key)
            else -> goToKey(key)
        }
    }

    fun goBack() {
        when(state.currentKey) {
            state.startKey -> error("You cannot go back from the start route")
            in state.topKeys -> error("You cannot go back from the top route")
            else -> state.currentSubStack.removeLastOrNull()
        }
    }
    fun goStart() {
        state.topStack.run {
            clear()
            add(state.startKey)
        }
    }

    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    private fun goToTop(key: NavKey) {
        state.topStack.apply {
            if (key == state.startKey) {
                clear()
            }else {
                remove(key)
            }
            add(key)
        }
    }

    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear() // 只留下最底层的，也就是currentTopKey
        }
    }
}