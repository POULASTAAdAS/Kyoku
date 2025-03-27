package com.poulastaa.explore.domain.model

import com.poulastaa.core.domain.model.AlbumId

sealed class ExploreAllowedNavigationScreen {
    data class Album(val albumId: AlbumId) : ExploreAllowedNavigationScreen()
}