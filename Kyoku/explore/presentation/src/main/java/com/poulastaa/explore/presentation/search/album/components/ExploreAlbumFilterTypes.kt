package com.poulastaa.explore.presentation.search.album.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppFilterChip
import com.poulastaa.explore.presentation.search.album.ExploreAlbumUiAction
import com.poulastaa.explore.presentation.search.album.SEARCH_ALBUM_FILTER_TYPE

@Composable
internal fun ExploreAlbumFilterTypes(
    filterType: SEARCH_ALBUM_FILTER_TYPE,
    onAction: (ExploreAlbumUiAction.OnFilterTypeChange) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        AppFilterChip(
            title = SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR.value,
            icon = ImageVector.vectorResource(SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR.icon),
            isSelected = filterType == SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR,
            onClick = {
                onAction(
                    ExploreAlbumUiAction.OnFilterTypeChange(
                        SEARCH_ALBUM_FILTER_TYPE.MOST_POPULAR
                    )
                )
            }
        )

        AppFilterChip(
            icon = ImageVector.vectorResource(SEARCH_ALBUM_FILTER_TYPE.ARTIST.icon),
            title = SEARCH_ALBUM_FILTER_TYPE.ARTIST.value,
            isSelected = filterType == SEARCH_ALBUM_FILTER_TYPE.ARTIST,
            onClick = {
                onAction(
                    ExploreAlbumUiAction.OnFilterTypeChange(
                        SEARCH_ALBUM_FILTER_TYPE.ARTIST
                    )
                )
            }
        )

        AppFilterChip(
            title = SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR.value,
            icon = ImageVector.vectorResource(SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR.icon),
            isSelected = filterType == SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR,
            onClick = {
                onAction(
                    ExploreAlbumUiAction.OnFilterTypeChange(
                        SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR
                    )
                )
            }
        )
    }
}
