package com.poulastaa.core.presentation.ui.model

data class ArtistUiSong(
    val id: Long = -1,
    val title: String = "",
    val coverImage: String = "",
    val isExpanded: Boolean = false,
    val popularity: Long = -1,
)
