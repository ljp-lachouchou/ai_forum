package ai.ljp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator


@Composable
fun rememberNavigationState(
    startKey: NavKey,
    topKeys : Set<NavKey>
) : NavigationState {
    val topStack = rememberNavBackStack(startKey)
    val subStacks = topKeys.associateWith { key -> rememberNavBackStack(key) }
    return remember(startKey,topKeys) {
        NavigationState(
            startKey = startKey,
            topStack = topStack,
            subStacks = subStacks
        )
    }
}

class NavigationState(
    val startKey : NavKey,
    val topStack : NavBackStack<NavKey>,
    val subStacks : Map<NavKey, NavBackStack<NavKey>>
) {
    //会被 @Composable 直接读取
    val currentTopKey : NavKey by derivedStateOf { topStack.last() }

    val topKeys
        get() = subStacks.keys

    val currentSubStack
        get() = subStacks[currentTopKey] ?: error("$currentTopKey 子栈不存在")

    //会被 @Composable 直接读取
    val currentKey : NavKey by derivedStateOf { currentSubStack.last() }
}
@Composable
fun NavigationState.toEntries(
    entryProvider : (NavKey) -> NavEntry<NavKey>
) : SnapshotStateList<NavEntry<NavKey>> {
    val decoratorEntries =subStacks.mapValues { (_,stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),

        )
        rememberDecoratedNavEntries(
            entryProvider = entryProvider,
            entryDecorators = decorators,
            backStack = stack
        )

    }
    return topStack
        .flatMap {
        decoratorEntries[it] ?: emptyList()
    }.toMutableStateList()
}