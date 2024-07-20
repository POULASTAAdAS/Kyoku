package com.poulastaa.play.presentation.root_drawer.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AddIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SortTypeGridIcon
import com.poulastaa.core.presentation.designsystem.SortTypeListIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.root_drawer.library.LibraryUiEvent
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryFilterRow(
    modifier: Modifier = Modifier,
    filterType: LibraryFilterType,
    viewType: LibraryViewType,
    onClick: (LibraryUiEvent) -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(.7f),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            FilterChip(
                selected = filterType == LibraryFilterType.ALL,
                onClick = { onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.ALL)) },
                label = {
                    Text(
                        text = stringResource(id = R.string.all),
                        fontWeight = if (filterType == LibraryFilterType.ALL) FontWeight.SemiBold else FontWeight.Normal,
                        letterSpacing = 1.sp
                    )
                },
                shape = MaterialTheme.shapes.extraSmall,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(.8f),
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                )
            )

            LibraryFilterChip(
                text = stringResource(id = R.string.album),
                icon = FilterAlbumIcon,
                selected = filterType == LibraryFilterType.ALBUM,
                onClick = {
                    onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.ALBUM))
                }
            )

            LibraryFilterChip(
                text = stringResource(id = R.string.artist),
                icon = FilterArtistIcon,
                selected = filterType == LibraryFilterType.ARTIST,
                onClick = {
                    onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.ARTIST))
                }
            )

            LibraryFilterChip(
                text = stringResource(id = R.string.playlist),
                icon = FilterPlaylistIcon,
                selected = filterType == LibraryFilterType.PLAYLIST,
                onClick = {
                    onClick(LibraryUiEvent.ToggleFilterType(LibraryFilterType.PLAYLIST))
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    onClick(LibraryUiEvent.ToggleView)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary.copy(.8f)
                )
            ) {
                Icon(
                    imageVector = if (viewType == LibraryViewType.LIST) SortTypeGridIcon else SortTypeListIcon,
                    contentDescription = null,
                )
            }
        }
    }
}


@Composable
fun LibraryHeader(
    modifier: Modifier = Modifier,
    header: String,
    onAddClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = header,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground.copy(.8f),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onAddClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary.copy(.8f)
                )
            ) {
                Icon(
                    imageVector = AddIcon,
                    contentDescription = null
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(.5f))
        )
    }
}

@Composable
private fun LibraryFilterChip(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                letterSpacing = 1.sp
            )
        },
        leadingIcon = {
            if (selected) Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        },
        shape = MaterialTheme.shapes.extraSmall,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(.8f),
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = MaterialTheme.dimens.medium1)
        ) {
            LibraryHeader(
                header = stringResource(id = R.string.artist),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.medium1)
            ) {

            }
        }
    }
}