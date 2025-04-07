package com.poulastaa.view.presentation.saved.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poulastaa.view.presentation.saved.ViewSavedUiItemType


@Composable
internal fun ViewSavedItemImageCard(
    modifier: Modifier = Modifier,
    type: ViewSavedUiItemType,
    poster: List<String>,
) {
    Card(
        modifier = modifier,
        shape = if (type == ViewSavedUiItemType.ARTIST) CircleShape
        else MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        if (type == ViewSavedUiItemType.PLAYLIST
            && poster.size > 3
        ) Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            ) {
                ViewSavedItemImage(
                    Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(),
                    poster.getOrNull(0)
                )
                ViewSavedItemImage(Modifier.fillMaxSize(), poster.getOrNull(1))
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ViewSavedItemImage(
                    Modifier
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(),
                    poster.getOrNull(2)
                )
                ViewSavedItemImage(Modifier.fillMaxSize(), poster.getOrNull(3))
            }
        }
        else ViewSavedItemImage(Modifier.fillMaxSize(), poster.firstOrNull())
    }
}