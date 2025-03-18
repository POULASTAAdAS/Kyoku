package com.poulastaa.core.domain.model

data class DtoViewOtherPayload<T>(
    val heading: DtoHeading,
    val songs: List<T>,
)
