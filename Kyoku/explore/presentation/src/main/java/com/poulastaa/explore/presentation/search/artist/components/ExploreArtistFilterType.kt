package com.poulastaa.explore.presentation.search.artist.components

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
import com.poulastaa.explore.presentation.search.artist.ExploreArtistUiAction
import com.poulastaa.explore.presentation.search.artist.SEARCH_ARTIST_FILTER_TYPE

@Composable
internal fun ExploreArtistFilterType(
    filterType: SEARCH_ARTIST_FILTER_TYPE,
    onAction: (ExploreArtistUiAction.OnFilterTypeChange) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        AppFilterChip(
            title = SEARCH_ARTIST_FILTER_TYPE.ALL.value,
            icon = ImageVector.vectorResource(SEARCH_ARTIST_FILTER_TYPE.ALL.icon),
            isSelected = filterType == SEARCH_ARTIST_FILTER_TYPE.ALL,
            onClick = {
                onAction(
                    ExploreArtistUiAction.OnFilterTypeChange(
                        SEARCH_ARTIST_FILTER_TYPE.ALL
                    )
                )
            }
        )

        AppFilterChip(
            title = SEARCH_ARTIST_FILTER_TYPE.INTERNATIONAL.value,
            icon = ImageVector.vectorResource(SEARCH_ARTIST_FILTER_TYPE.INTERNATIONAL.icon),
            isSelected = filterType == SEARCH_ARTIST_FILTER_TYPE.INTERNATIONAL,
            onClick = {
                onAction(
                    ExploreArtistUiAction.OnFilterTypeChange(
                        SEARCH_ARTIST_FILTER_TYPE.INTERNATIONAL
                    )
                )
            }
        )
    }
}