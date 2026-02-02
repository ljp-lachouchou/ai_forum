package com.ljp.model

import kotlinx.datetime.Instant

data class Notification(
    val id : String,
    val userId : String,
    val type : String,
    val content : String,
    val refType : String?,
    val refId : String?,
    val read : Boolean,
    val createdAt : Instant
)