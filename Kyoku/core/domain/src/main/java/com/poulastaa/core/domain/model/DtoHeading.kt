package com.poulastaa.core.domain.model

data class DtoHeading(
    val type: ViewType,
    val id: Long = -1,
    val name: String,
    val poster: String = "",
)