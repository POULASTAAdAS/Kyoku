package com.poulastaa.kyoku.presentation.screen.setup.suggest_genre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.UiGenre
import com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.components.SuggestGenreItem

@Composable
fun SuggestGenreContent(
    paddingValues: PaddingValues,
    data: List<UiGenre>,
    maxGrid: Int,
    onGenreClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        columns = GridCells.Fixed(maxGrid),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = data,
            key = {
                it.name
            }
        ) {
            SuggestGenreItem(
                modifier = Modifier
                    .fillMaxWidth(),
                uiGenre = it,
                clicked = {
                    onGenreClicked.invoke(it.name)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SuggestGenreContent(
        paddingValues = PaddingValues(),
        data = listOf(
            UiGenre(
                name = "name1",
                isSelected = true
            ), UiGenre(
                name = "name2",
                isSelected = true
            ), UiGenre(
                name = "name3",
                isSelected = true
            ), UiGenre(
                name = "name4",
                isSelected = true
            ), UiGenre(
                name = "name5",
                isSelected = true
            ), UiGenre(
                name = "name6",
                isSelected = true
            ), UiGenre(
                name = "name7",
                isSelected = true
            ), UiGenre(
                name = "name8",
                isSelected = true
            ), UiGenre(
                name = "name9",
                isSelected = true
            ), UiGenre(
                name = "name10",
                isSelected = true
            ), UiGenre(
                name = "name11",
                isSelected = true
            ), UiGenre(
                name = "name12",
                isSelected = true
            ), UiGenre(
                name = "name13",
                isSelected = true
            ), UiGenre(
                name = "name14",
                isSelected = true
            ), UiGenre(
                name = "name15",
                isSelected = true
            ), UiGenre(
                name = "name16",
                isSelected = true
            ), UiGenre(
                name = "name17",
                isSelected = true
            ), UiGenre(
                name = "name18",
                isSelected = true
            ), UiGenre(
                name = "name19",
                isSelected = true
            ), UiGenre(
                name = "name20",
                isSelected = true
            ), UiGenre(
                name = "name21",
                isSelected = true
            ), UiGenre(
                name = "name22",
                isSelected = true
            ), UiGenre(
                name = "name23",
                isSelected = true
            )
        ),
        maxGrid = 2,
        onGenreClicked = {}
    )
}