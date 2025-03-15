package com.poulastaa.core.domain.model

data class DtoViewOtherPayload(
    val heading: DtoHeading,
    val songs: List<DtoDetailedPrevSong>,
)
