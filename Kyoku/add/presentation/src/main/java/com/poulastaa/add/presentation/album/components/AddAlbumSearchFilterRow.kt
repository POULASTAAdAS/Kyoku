package com.poulastaa.add.presentation.album.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.poulastaa.add.presentation.album.AddAlbumSearchUiFilterType
import com.poulastaa.add.presentation.album.AddAlbumUiAction
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppFilterChip


@Composable
internal fun AddAlbumSearchFilterRow(
    modifier: Modifier = Modifier,
    searchFilterType: AddAlbumSearchUiFilterType,
    onAction: (AddAlbumUiAction.OnSearchFilterTypeChange) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        AppFilterChip(
            title = stringResource(AddAlbumSearchUiFilterType.MOST_POPULAR.value),
            isSelected = AddAlbumSearchUiFilterType.MOST_POPULAR == searchFilterType,
            icon = ImageVector.vectorResource(AddAlbumSearchUiFilterType.MOST_POPULAR.icon),
            onClick = {
                onAction(
                    AddAlbumUiAction.OnSearchFilterTypeChange(
                        AddAlbumSearchUiFilterType.MOST_POPULAR
                    )
                )
            }
        )

        AppFilterChip(
            title = stringResource(AddAlbumSearchUiFilterType.RELEASE_YEAR.value),
            isSelected = AddAlbumSearchUiFilterType.RELEASE_YEAR == searchFilterType,
            icon = ImageVector.vectorResource(AddAlbumSearchUiFilterType.RELEASE_YEAR.icon),
            onClick = {
                onAction(
                    AddAlbumUiAction.OnSearchFilterTypeChange(
                        AddAlbumSearchUiFilterType.RELEASE_YEAR
                    )
                )
            }
        )
    }
}