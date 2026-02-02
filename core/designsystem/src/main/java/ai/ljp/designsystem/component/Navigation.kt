package ai.ljp.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AIForumNavigationSuiteScaffold(
    navigationSuiteItems : AIForumNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content : @Composable () -> Unit
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    val navigationSuiteItemColors = NavigationSuiteItemColors( //统一三种尺寸类型下的颜色
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = AIForumNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AIForumNavigationDefaults.navigationContentColor(),
            selectedTextColor = AIForumNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AIForumNavigationDefaults.navigationContentColor(),
            indicatorColor = AIForumNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = AIForumNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AIForumNavigationDefaults.navigationContentColor(),
            selectedTextColor = AIForumNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AIForumNavigationDefaults.navigationContentColor(),
            indicatorColor = AIForumNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = AIForumNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AIForumNavigationDefaults.navigationContentColor(),
            selectedTextColor = AIForumNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AIForumNavigationDefaults.navigationContentColor(),
        ),
    )
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AIForumNavigationSuiteScope(
                this,
                navigationSuiteItemColors
            )
                .run(navigationSuiteItems)
        },
        layoutType = layoutType,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContentColor = AIForumNavigationDefaults.navigationContentColor(),
            navigationRailContainerColor = Color.Transparent,
        ),
        modifier = modifier,
        content = content
    )
}
class AIForumNavigationSuiteScope(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors
) {
    fun item(
        selected : Boolean,
        onClick : () -> Unit,
        modifier : Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        colors = navigationSuiteItemColors,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
    )
}
object AIForumNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onPrimary

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}