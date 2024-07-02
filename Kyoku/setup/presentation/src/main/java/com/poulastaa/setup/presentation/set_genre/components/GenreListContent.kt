package com.poulastaa.setup.presentation.set_genre.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.set_genre.model.UiGenre

@Composable
fun GenreListContent(
    grid: Int,
    data: List<UiGenre>,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
    onClick: (id: Int) -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(grid),
        contentPadding = PaddingValues(vertical = MaterialTheme.dimens.medium1),
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items(
            items = data,
            key = {
                it.id
            }
        ) {
            GenreItem(
                modifier = Modifier.clickable(
                    onClick = {
                        onClick(it.id)
                    },
                    interactionSource = interactionSource,
                    indication = null
                ),
                genre = it
            )
        }
    }
}