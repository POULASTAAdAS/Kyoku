package com.poulastaa.kyoku.presentation.screen.setup.select_genre.components

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

@Composable
fun SuggestGenreItem(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ),
    clicked: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = clicked)
            .height(80.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = colors
    ) {
        val color = MaterialTheme.colorScheme.primary

        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = modifier.padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Canvas(modifier = Modifier) {
                    if (isSelected)
                        drawCircle(
                            color = color,
                            radius = 8f
                        )
                }
            }

            Text(
                text = name,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
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
        name = "Hindi",
        isSelected = true,
        modifier = Modifier.width(80.dp)
    ) {

    }
}