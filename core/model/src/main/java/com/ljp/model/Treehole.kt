package com.ljp.model

import kotlinx.datetime.Instant

data class Treehole(
    val id : String,
    val authorId : String?,
    val content : String,
    val anonymous : Boolean,
    val createAt : Instant,
)