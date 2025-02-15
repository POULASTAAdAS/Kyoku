package com.poulastaa.setup.presentation.pic_artist

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.model.TextHolder

data class PicArtistUiState(
    val isMakingApiCall: Boolean = false,
    val artistQuery: TextHolder = TextHolder(),
    val data: List<ArtistId> = emptyList(),
) {
    val isMinLimitReached = data.size >= 5
}

data class UiArtist(
    val id: ArtistId,
    val name: String,
    val cover: String? = null,
    val isSelected: Boolean = false,
)
