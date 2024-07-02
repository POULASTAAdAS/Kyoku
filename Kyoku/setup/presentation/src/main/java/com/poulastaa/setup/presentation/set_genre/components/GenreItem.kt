package com.poulastaa.setup.presentation.set_genre.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.set_genre.model.UiGenre

@Composable
fun GenreItem(
    modifier: Modifier = Modifier,
    genre: UiGenre,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = genre.color.copy(.4f),
        ),
        border = if (genre.isSelected) BorderStroke(
            width = 1.5.dp,
            color = genre.color
        ) else null
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = genre.name,
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.medium1)
                    .padding(horizontal = MaterialTheme.dimens.medium3)
                    .align(Alignment.Center),
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )

            Canvas(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = MaterialTheme.dimens.medium1)
                    .padding(end = MaterialTheme.dimens.medium1)
            ) {
                if (genre.isSelected) drawCircle(
                    color = genre.color,
                    radius = 13f
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.large1)
        ) {
            GenreItem(
                genre = UiGenre(
                    isSelected = true,
                    name = "Genre"
                )
            )
        }
    }
}