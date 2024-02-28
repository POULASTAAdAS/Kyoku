package com.poulastaa.kyoku.presentation.screen.setup.suggest_artist.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.UiArtist

@Composable
fun SuggestArtistItem(
    modifier: Modifier = Modifier,
    uiArtist: UiArtist,
    isCookie: Boolean,
    authHeader: String,
    clicked: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(
                onClick = clicked,
                interactionSource = MutableInteractionSource(),
                indication = null
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        val isDarkThem = isSystemInDarkTheme()

        Column(
            modifier = Modifier
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiArtist.profileUrl)
                    .addHeader(
                        name = if (isCookie) "Cookie" else "Authorization",
                        value = authHeader
                    ).fallback(
                        drawableResId = if (isDarkThem) R.drawable.night_logo
                        else R.drawable.light_logo
                    )
                    .error(
                        drawableResId = if (isDarkThem) R.drawable.night_logo
                        else R.drawable.light_logo
                    )
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(
                    id = if (isDarkThem) R.drawable.night_logo
                    else R.drawable.light_logo
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (uiArtist.isSelected) 5.dp else 0.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentDescription = null
            )

            Text(
                text = uiArtist.name,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SuggestArtistItem(
        modifier = Modifier,
        uiArtist = UiArtist(
            name = "Arijit Singh",
            isSelected = true
        ),
        isCookie = false,
        authHeader = "",
        clicked = {}
    )
}