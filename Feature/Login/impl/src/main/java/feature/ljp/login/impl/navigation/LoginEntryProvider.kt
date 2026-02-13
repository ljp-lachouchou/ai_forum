package feature.ljp.login.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.home.api.navigateToHome
import feature.ljp.login.api.LoginKey
import feature.ljp.login.impl.LoginScreen

fun EntryProviderScope<NavKey>.loginEntry(navigator: Navigator) {
    entry<LoginKey> {
        LoginScreen(
            onHomeClick = navigator::navigateToHome
        )
    }
}