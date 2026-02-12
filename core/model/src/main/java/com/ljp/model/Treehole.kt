package com.ljp.model

import kotlinx.datetime.Instant

data class Treehole(
    val id : String,
    val authorId : String?,
    val content : String,
    val anonymous : Boolean,
    val mood : String,
    val createdAt : Instant,
)

data class TreeholeProfileSource(
    val author : Profile,
    val id : String,
    val content : String,
    val anonymous : Boolean,
    val createdAt : Instant,
    val mood : String,
)