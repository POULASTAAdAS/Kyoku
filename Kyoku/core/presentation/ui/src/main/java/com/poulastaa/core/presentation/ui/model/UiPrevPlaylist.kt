package com.poulastaa.core.presentation.ui.model

data class UiPrevPlaylist(
    val id: Long = -1,
    val name: String = "",
    val urls: List<String> = emptyList(),
)
