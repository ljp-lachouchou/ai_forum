package feature.ljp.post.api

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class PostNavKey(
    val postId : String
) : NavKey

fun Navigator.navigateToPost(
    postId : String
) = navigate(PostNavKey(postId = postId))