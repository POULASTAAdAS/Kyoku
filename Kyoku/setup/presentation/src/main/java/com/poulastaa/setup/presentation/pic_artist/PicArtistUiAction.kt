package com.poulastaa.setup.presentation.pic_artist

import com.poulastaa.core.domain.model.ArtistId

sealed interface PicArtistUiAction {
    data class OnQueryChange(val query: String) : PicArtistUiAction
    data class OnArtistToggle(val artistId: ArtistId, val state: Boolean) : PicArtistUiAction
    data object OnSubmit : PicArtistUiAction
}