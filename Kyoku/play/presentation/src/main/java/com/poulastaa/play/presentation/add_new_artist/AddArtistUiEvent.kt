package com.poulastaa.play.presentation.add_new_artist

import com.poulastaa.core.domain.model.ArtistPagingType

sealed interface AddArtistUiEvent {
    data class OnArtistClick(val artistId: Long) : AddArtistUiEvent
    data object OnSearchToggle : AddArtistUiEvent
    data class OnSearchQueryChange(val query: String) : AddArtistUiEvent

    data class OnFilterTypeChange(val type: ArtistPagingType) : AddArtistUiEvent

    data object OnMassSelectToggle : AddArtistUiEvent
    data class OnCheckChange(val id: Long, val status: Boolean) : AddArtistUiEvent
    data object OnSaveClick : AddArtistUiEvent
}