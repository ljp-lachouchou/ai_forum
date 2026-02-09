package feature.ljp.profile.api

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ProfileNavKey(
    val profileId : String
) : NavKey

fun Navigator.navigateToProfile(
    profileId : String
) {
    navigate(ProfileNavKey(profileId))
}