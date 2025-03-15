package com.poulastaa.core.domain.model

data class DtoViewPayload<T>(
    val heading: DtoHeading,
    val listOfSongs: List<T> = emptyList(),
)