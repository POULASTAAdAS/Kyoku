package com.poulastaa.kyoku.presentation.screen.setup.suggest_artist

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
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.UiArtist
import com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.components.SuggestArtistItem

@Composable
fun SuggestArtistContent(
    paddingValues: PaddingValues,
    maxGrid: Int,
    data: List<UiArtist>,
    isCookie: Boolean,
    authHeader: String,
    onArtistClicked: (String) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(maxGrid),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 80.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = data,
            key = {
                it.name
            }
        ) {
            SuggestArtistItem(
                modifier = Modifier.fillMaxWidth(),
                uiArtist = it,
                isCookie = isCookie,
                authHeader = authHeader,
                clicked = {
                    onArtistClicked.invoke(it.name)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    SuggestArtistContent(
        paddingValues = PaddingValues(),
        maxGrid = 3,
        data = listOf(
            UiArtist(
                name = "name1",
                isSelected = false
            ), UiArtist(
                name = "name2",
                isSelected = false
            ), UiArtist(
                name = "name3",
                isSelected = false
            ), UiArtist(
                name = "name4",
                isSelected = true
            ), UiArtist(
                name = "name5",
                isSelected = false
            ), UiArtist(
                name = "name6",
                isSelected = true
            ), UiArtist(
                name = "name7",
                isSelected = false
            ), UiArtist(
                name = "name8",
                isSelected = true
            ), UiArtist(
                name = "name9",
                isSelected = true
            ), UiArtist(
                name = "name10",
                isSelected = true
            ), UiArtist(
                name = "name11",
                isSelected = false
            ), UiArtist(
                name = "name12",
                isSelected = false
            ), UiArtist(
                name = "name13",
                isSelected = true
            ), UiArtist(
                name = "name14",
                isSelected = false
            ), UiArtist(
                name = "name15",
                isSelected = false
            ), UiArtist(
                name = "name16",
                isSelected = false
            ), UiArtist(
                name = "name17",
                isSelected = false
            )
        ),
        isCookie = false,
        authHeader = "",
        onArtistClicked = {}
    )
}