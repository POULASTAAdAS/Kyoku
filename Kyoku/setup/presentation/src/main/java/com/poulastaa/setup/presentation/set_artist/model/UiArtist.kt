package com.poulastaa.setup.presentation.set_artist.model

data class UiArtist(
    val id: Long = -1,
    val name:String = "",
    val coverImageUrl: String = "",
    val isSelected: Boolean = false
)
