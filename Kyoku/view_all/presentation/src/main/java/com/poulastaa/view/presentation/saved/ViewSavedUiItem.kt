package com.poulastaa.view.presentation.saved

internal data class ViewSavedUiItem(
    val id: Long,
    val title: String,
    val poster: List<String> = emptyList(),
    val artist: String? = null,
    val releaseYear: Int? = null,
    val numbers: Long = 0,
)
