package ai.ljp.aiforum.ui

import ai.ljp.aiforum.R
import ai.ljp.aiforum.navigation.TOP_LEVEL_NAV_ITEMS
import ai.ljp.designsystem.component.AIForumBackground
import ai.ljp.designsystem.component.AIForumGradientBackground
import ai.ljp.designsystem.component.AIForumNavigationSuiteScaffold
import ai.ljp.designsystem.component.AIForumTopAppBar
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.designsystem.theme.GradientColors
import ai.ljp.designsystem.theme.LocalGradientColors
import ai.ljp.navigation.Navigator
import ai.ljp.navigation.toEntries
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import feature.ljp.home.api.HomeKey
import feature.ljp.home.api.navigateToHome
import feature.ljp.home.impl.navigation.homeEntry
import feature.ljp.login.impl.LoginScreen
import feature.ljp.login.impl.navigation.loginEntry
import feature.ljp.me.impl.navigation.meEntry
import feature.ljp.post.impl.navigation.postEntry
import feature.ljp.postcreate.impl.navigation.postCreateEntry
import feature.ljp.search.api.SearchNavKey
import feature.ljp.search.impl.navigation.searchEntry
import feature.ljp.treehole.impl.navigation.treeholeEntry
import featurei.ljp.profile.impl.navigation.profileEntry
import featurei.ljp.treeholecreate.impl.navigation.treeholeCreateEntry

@Composable
fun AiForumApp(
    appState: AIForumAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo : WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val shouldShowGradientBackground = appState.navigationState.currentTopKey == HomeKey
    AIForumBackground(
        modifier = modifier
    ) {
        AIForumGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            }
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
            val notConnectedMessage = stringResource(R.string.not_connected)
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = notConnectedMessage,
                        duration = Indefinite,
                    )
                }
            }
            CompositionLocalProvider(
                LocalSnackbarHostState provides snackbarHostState
            ) {
                AIForumApp(
                    appState = appState,
                    windowAdaptiveInfo =windowAdaptiveInfo
                )
            }

        }
    }
}
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AIForumApp(
    appState : AIForumAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val isLogin by appState.isLogin.collectAsStateWithLifecycle()
    val navigator = remember { Navigator(appState.navigationState) }
    if (!isLogin) {
        LoginScreen(
            onHomeClick = navigator::navigateToHome
        )
        return
    }
    val shouldShowNavigationSuite =
        appState.navigationState.currentKey in appState.navigationState.topKeys

    val appContent: @Composable () -> Unit = {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0,0,0,0),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.exclude(
                            WindowInsets.ime,
                        ),
                    )
                )
            }
        ) {padding->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding) // 已经将padding里面蕴含的windowInset消耗了
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                var shouldTopbar = false
                if (appState.navigationState.currentKey in appState.navigationState.topKeys) {
                    shouldTopbar = true
                    val destination = TOP_LEVEL_NAV_ITEMS[appState.navigationState.currentTopKey]
                        ?: error("Top level nav item not found for ${appState.navigationState.currentTopKey}")
                    AIForumTopAppBar(
                        titleRes = destination.titleTextId,
                        navigationIcon = AIForumIcon.Search,
                        actionIcon = AIForumIcon.Settings,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                        onNavigationClick = { navigator.navigate(SearchNavKey) },
                    )
                }
                Box(
                    modifier = Modifier.consumeWindowInsets(
                        if (shouldTopbar) {
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Top
                            )
                        }else {
                            WindowInsets(0,0,0,0)
                        }
                    )
                ) {
                    val listDetailSceneStrategy = rememberListDetailSceneStrategy<NavKey>()
                    val entryProvider = entryProvider {
                        loginEntry(navigator)
                        homeEntry(navigator)
                        treeholeEntry(navigator)
                        meEntry(navigator)
                        postEntry(navigator)
                        profileEntry(navigator)
                        postCreateEntry(navigator)
                        treeholeCreateEntry(navigator)
                        searchEntry(navigator)
                    }
                    NavDisplay(
                        entries =appState.navigationState.toEntries(entryProvider),
                        sceneStrategy = listDetailSceneStrategy,
                        onBack = { navigator.goBack() }
                    )
                }
            }

        }
    }

    if (shouldShowNavigationSuite) {
        AIForumNavigationSuiteScaffold(
            navigationSuiteItems = {
                TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                    val selected = navKey == appState.navigationState.currentTopKey
                    item(
                        selected = selected,
                        onClick = { navigator.navigate(navKey) },
                        icon = {
                            Icon(
                                imageVector = navItem.unselectedIcon,
                                contentDescription = null,
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = navItem.selectedIcon,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(stringResource(navItem.iconTextId))
                        }
                    )
                }
            },
            windowAdaptiveInfo = windowAdaptiveInfo
        ) {
            appContent()
        }
    } else {
        appContent()
    }
}
