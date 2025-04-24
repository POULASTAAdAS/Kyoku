package com.poulastaa.add.presentation.artist

internal sealed interface AddArtistUiAction {
    data class OnItemClick(val artist: UiArtist) : AddArtistUiAction
    data class OnFilterTypeChange(val filterType: AddArtistSearchUiFilterType) : AddArtistUiAction
    data class OnSearchQueryChange(val query: String) : AddArtistUiAction
    data object OnSaveClick : AddArtistUiAction
    data object OnCancelSaveClick : AddArtistUiAction
}