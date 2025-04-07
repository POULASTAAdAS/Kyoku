package com.poulastaa.view.domain.model

data class DtoViewSavedItem(
    val id: Long,
    val title: String,
    val poster: List<String> = emptyList(),
    val artist: String? = null,
    val releaseYear: Int? = null,
    val numbers: Long = 0,
)
