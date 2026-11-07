package com.poulastaa.setup.domain.model

data class DtoSong(
    val id: Long,
    val title: String,
    val poster: String? = null,
    val artists: List<String> = emptyList(),
)
