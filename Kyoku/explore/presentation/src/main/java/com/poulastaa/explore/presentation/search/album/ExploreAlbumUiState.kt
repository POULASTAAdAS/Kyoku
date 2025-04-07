package com.poulastaa.explore.presentation.search.album

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder

internal data class ExploreAlbumUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val filterType: SEARCH_ALBUM_FILTER_TYPE = SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR,
    val isSearchOpen: Boolean = false,
    val query: TextHolder = TextHolder(),
)

internal enum class SEARCH_ALBUM_FILTER_TYPE(@StringRes val value: Int, @DrawableRes val icon: Int) {
    MOST_POPULAR(R.string.most_popular, R.drawable.ic_popular),
    ARTIST(R.string.artist, R.drawable.ic_user),
    RELEASE_YEAR(R.string.release_year, R.drawable.ic_b_date),
}