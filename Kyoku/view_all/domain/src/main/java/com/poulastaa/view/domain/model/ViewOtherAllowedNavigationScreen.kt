package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.ArtistId

sealed interface ViewOtherAllowedNavigationScreen {
    data class Artist(val artistId: ArtistId) : ViewOtherAllowedNavigationScreen
}