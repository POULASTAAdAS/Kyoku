package com.poulastaa.core.domain.model

data class DtoHeading(
    val type: DtoViewType,
    val id: Long = -1,
    val name: String,
    val poster: String = "",
)