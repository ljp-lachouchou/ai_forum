package ai.ljp.aiforum.navigation

import ai.ljp.designsystem.icon.AIForumIcon
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import feature.ljp.home.api.HomeKey
import feature.ljp.me.api.MeNavKey
import feature.ljp.treehole.api.TreeholeKey
import feature.ljp.me.api.R as meR
import feature.ljp.home.api.R as homeR
import feature.ljp.treehole.api.R as treeholeR
import ai.ljp.aiforum.R
data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)
val HOME = TopLevelNavItem(
    selectedIcon = AIForumIcon.Home,
    unselectedIcon = AIForumIcon.AddHomeWork,
    iconTextId = homeR.string.feature_home_api_home_title,
    titleTextId = R.string.app_name,
)

val TREEHOLE = TopLevelNavItem(
    selectedIcon = AIForumIcon.Treehole,
    unselectedIcon = AIForumIcon.Streetview,
    iconTextId = treeholeR.string.feature_treehole_api_title,
    titleTextId = treeholeR.string.feature_treehole_api_title,
)

val ME = TopLevelNavItem(
    selectedIcon = AIForumIcon.Person,
    unselectedIcon = AIForumIcon.PersonOutline,
    iconTextId = meR.string.feature_me_api_person_title,
    titleTextId = meR.string.feature_me_api_person_title,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeKey to HOME,
    TreeholeKey to TREEHOLE,
    MeNavKey to ME,
)