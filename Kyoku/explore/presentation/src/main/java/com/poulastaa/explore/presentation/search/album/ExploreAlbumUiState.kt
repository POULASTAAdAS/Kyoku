package com.poulastaa.explore.presentation.search.album

import androidx.annotation.DrawableRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.model.UiPrevAlbum

internal data class ExploreAlbumUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val album: UiPrevAlbum = UiPrevAlbum(),
    val filterType: SEARCH_ALBUM_FILTER_TYPE = SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR,
    val isSearchOpen: Boolean = false,
    val query: TextHolder = TextHolder(),
)

internal enum class SEARCH_ALBUM_FILTER_TYPE(val value: String, @DrawableRes val icon: Int) {
    MOST_POPULAR("most popular", R.drawable.ic_popular),
    ARTIST("artist", R.drawable.ic_user),
    RELEASE_YEAR("release year", R.drawable.ic_b_date),
}