package com.poulastaa.core.presentation.ui.model

data class UiPrevSong(
    val id: Long = -1,
    val title: String = "",
    val poster: String? = null,
    val artists: String? = null,
    val releaseYear: Int? = null,
)
