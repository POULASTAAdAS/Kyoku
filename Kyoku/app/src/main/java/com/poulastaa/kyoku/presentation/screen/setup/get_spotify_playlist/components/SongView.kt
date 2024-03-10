package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun SpotifyPlaylistSongCard(
    modifier: Modifier,
    imageUrl: String,
    title: String,
    isCookie: Boolean,
    headerValue: String,
    artist: String
) {
    Card(
        modifier = modifier
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.dimens.small2),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .addHeader(
                        name = if (isCookie) "Cookie" else "Authorization",
                        value = headerValue
                    )
                    .fallback(
                        drawableResId = if (isSystemInDarkTheme()) R.drawable.night_logo
                        else R.drawable.light_logo
                    )
                    .error(
                        drawableResId = if (isSystemInDarkTheme()) R.drawable.night_logo
                        else R.drawable.light_logo
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(
                    id = if (isSystemInDarkTheme()) R.drawable.night_logo
                    else R.drawable.light_logo
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

            Column {
                Text(
                    text = title,
                    modifier = Modifier.padding(MaterialTheme.dimens.small1),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                )

                Text(
                    text = artist,
                    modifier = Modifier.padding(MaterialTheme.dimens.small1),
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Preview() {
    TestThem {
        SpotifyPlaylistSongCard(
            modifier = Modifier.fillMaxWidth(),
            imageUrl = "",
            title = "Title",
            isCookie = false,
            headerValue = "",
            artist = "Artist"
        )
    }
}