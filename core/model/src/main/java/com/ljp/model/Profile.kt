package com.ljp.model

import kotlinx.datetime.Instant

data class Profile(
    val id : String,
    val userName : String,
    val avatarUrl : String?,
    val bio : String?,
    val role : String,
    val createdAt : Instant,
    val profileCount : String?
)
val Profile.hashId
    get() = id.reversed().hashCode()
val Profile.anonymousName
    get() = "匿名#$hashId"
data class ProfileWithFollows internal constructor(
    val id : String,
    val userName : String,
    val avatarUrl : String?,
    val bio : String?,
    val role : String,
    val createdAt : Instant,
    val follows : List<Profile>?,
    val profileCount : String?
) {
    constructor(user: Profile,follows : List<Profile>?) : this(
        id = user.id,
        userName = user.userName,
        avatarUrl = user.avatarUrl,
        bio = user.bio,
        role = user.role,
        createdAt = user.createdAt,
        follows = follows,
        profileCount = user.profileCount
    )
}
fun List<Profile>.asProfileWithFollows(follows: List<Profile>) = when {
    follows.isEmpty() -> map { ProfileWithFollows(it,null) }
    else -> map { ProfileWithFollows(it,follows) }
}