package com.poulastaa.core.domain.model

data class DtoDBArtist(
    val id: Long,
    val name: String,
    val coverImage: String?,
    val popularity: Long = 0,
)
