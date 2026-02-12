package feature.ljp.home.api

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object HomeKey : NavKey

fun Navigator.navigateToHome() {
    navigate(HomeKey)
}