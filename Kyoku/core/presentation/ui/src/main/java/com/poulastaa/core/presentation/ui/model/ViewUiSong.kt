package com.poulastaa.core.presentation.ui.model

data class ViewUiSong(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val isExpanded: Boolean = false,
)