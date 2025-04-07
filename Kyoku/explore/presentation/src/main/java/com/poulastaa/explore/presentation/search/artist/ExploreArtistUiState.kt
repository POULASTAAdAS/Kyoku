package com.poulastaa.explore.presentation.search.artist

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder

internal data class ExploreArtistUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val query: TextHolder = TextHolder(),
    val isSearchOpen: Boolean = false,
    val filterType: SEARCH_ARTIST_FILTER_TYPE = SEARCH_ARTIST_FILTER_TYPE.ALL,
)

internal enum class SEARCH_ARTIST_FILTER_TYPE(@StringRes val value: Int, @DrawableRes val icon: Int) {
    ALL(R.string.all, R.drawable.ic_filter_all),
    POPULARITY(R.string.by_popularity, R.drawable.ic_popular),
}