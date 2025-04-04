package com.poulastaa.explore.presentation.search.artist

import androidx.annotation.DrawableRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder

internal data class ExploreArtistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val query: TextHolder = TextHolder(),
    val isSearchOpen: Boolean = false,
    val filterType: SEARCH_ARTIST_FILTER_TYPE = SEARCH_ARTIST_FILTER_TYPE.ALL,
)

internal enum class SEARCH_ARTIST_FILTER_TYPE(val value: String, @DrawableRes val icon: Int) {
    ALL("all", R.drawable.ic_popular),
    INTERNATIONAL("international", R.drawable.ic_international),
}