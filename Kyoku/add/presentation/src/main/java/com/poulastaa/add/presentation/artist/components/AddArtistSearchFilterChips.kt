package com.poulastaa.add.presentation.artist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.poulastaa.add.presentation.artist.AddArtistSearchUiFilterType
import com.poulastaa.add.presentation.artist.AddArtistUiAction
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppFilterChip

@Composable
internal fun AddArtistSearchFilterChips(
    modifier: Modifier = Modifier,
    searchFilterType: AddArtistSearchUiFilterType,
    onAction: (AddArtistUiAction.OnFilterTypeChange) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
    ) {
        AppFilterChip(
            isSelected = AddArtistSearchUiFilterType.ALL == searchFilterType,
            title = stringResource(AddArtistSearchUiFilterType.ALL.value),
            icon = ImageVector.vectorResource(AddArtistSearchUiFilterType.ALL.icon),
            onClick = {
                onAction(
                    AddArtistUiAction.OnFilterTypeChange(
                        AddArtistSearchUiFilterType.ALL
                    )
                )
            }
        )

        AppFilterChip(
            isSelected = AddArtistSearchUiFilterType.INTERNATIONAL == searchFilterType,
            title = stringResource(AddArtistSearchUiFilterType.INTERNATIONAL.value),
            icon = ImageVector.vectorResource(AddArtistSearchUiFilterType.INTERNATIONAL.icon),
            onClick = {
                onAction(
                    AddArtistUiAction.OnFilterTypeChange(
                        AddArtistSearchUiFilterType.INTERNATIONAL
                    )
                )
            }
        )
    }
}