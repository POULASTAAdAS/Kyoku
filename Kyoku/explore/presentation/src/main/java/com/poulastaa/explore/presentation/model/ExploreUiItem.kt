package com.poulastaa.explore.presentation.model

internal data class ExploreUiItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String = "",
    val releaseYear: Int = 0,
    val artist: String? = null,
)