package com.poulastaa.main.presentation.library.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.poulastaa.main.presentation.library.UiLibraryFilterType

@Composable
internal fun LibraryFilterChip(
    type: UiLibraryFilterType,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ElevatedFilterChip(
        modifier = Modifier.animateContentSize(tween(400)),
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(type.id)
            )
        },
        colors = FilterChipDefaults.elevatedFilterChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = ImageVector.vectorResource(type.icon),
                    contentDescription = type.name,
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }
        } else null,
        elevation = FilterChipDefaults.elevatedFilterChipElevation(
            elevation = if (selected) 8.dp else 0.dp,
            pressedElevation = 0.dp
        )
    )
}