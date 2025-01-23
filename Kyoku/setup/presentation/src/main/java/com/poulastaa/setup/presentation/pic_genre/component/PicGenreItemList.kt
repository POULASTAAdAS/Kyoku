package com.poulastaa.setup.presentation.pic_genre.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.paging.compose.LazyPagingItems
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.pic_genre.PicGenreUiAction
import com.poulastaa.setup.presentation.pic_genre.UiGenre

@Composable
fun PicGenreItemList(
    gridSize: Int,
    genre: LazyPagingItems<UiGenre>,
    cardHeight: Dp,
    onAction: (PicGenreUiAction) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.dimens.small1),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
        columns = GridCells.Fixed(gridSize)
    ) {
        items(genre.itemCount) { index ->
            genre[index]?.let { uiGenre ->
                GenreCard(
                    modifier = Modifier.height(cardHeight),
                    genre = uiGenre,
                    onClick = {
                        onAction(
                            PicGenreUiAction.OnGenreSelect(
                                id = it,
                                isSelected = uiGenre.isSelected
                            )
                        )
                    }
                )
            }
        }
    }
}