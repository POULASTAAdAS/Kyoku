package com.poulastaa.kyoku.presentation.screen.setup.suggest_genre.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.UiGenre
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun SuggestGenreItem(
    modifier: Modifier = Modifier,
    uiGenre: UiGenre,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = uiGenre.color
    ),
    clicked: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = clicked)
            .height(80.dp),
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = colors
    ) {
        val color = MaterialTheme.colorScheme.tertiaryContainer

        Column(
            modifier = Modifier.padding(MaterialTheme.dimens.small1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = modifier.padding(MaterialTheme.dimens.small2),
                contentAlignment = Alignment.TopEnd
            ) {
                Canvas(modifier = Modifier) {
                    if (uiGenre.isSelected)
                        drawCircle(
                            color = color,
                            radius = 16f
                        )
                }
            }

            Text(
                text = uiGenre.name,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SuggestGenreItem(
        uiGenre = UiGenre(
            name = "Hindi",
            isSelected = true
        ),
        modifier = Modifier.width(80.dp)
    ) {

    }
}