package com.poulastaa.core.presentation.ui.components.crate_playlist

import com.poulastaa.core.presentation.designsystem.model.TextHolder

internal data class CreatePlaylistUiState(
    val isLoading: Boolean = false,
    val name: TextHolder = TextHolder(),
)
