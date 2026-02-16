package feature.ljp.postcreate.api

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object PostCreateNavKey : NavKey
fun Navigator.navigateToPostCreate() {
    navigate(PostCreateNavKey)
}