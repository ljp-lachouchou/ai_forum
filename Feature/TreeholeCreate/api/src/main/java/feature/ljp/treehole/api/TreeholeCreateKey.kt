package feature.ljp.treehole.api

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object TreeholeCreateKey : NavKey
fun Navigator.navigateToTreeholeCreate() {
    navigate(TreeholeCreateKey)
}