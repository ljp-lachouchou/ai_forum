package feature.ljp.login.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class LoginKey(val authToken : String?) : NavKey
