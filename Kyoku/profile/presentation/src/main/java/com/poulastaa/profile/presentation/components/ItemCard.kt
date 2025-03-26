package com.poulastaa.profile.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.profile.presentation.UiItemType
import com.poulastaa.profile.presentation.UiSavedItems

@Composable
internal fun ItemCard(
    modifier: Modifier,
    item: UiSavedItems,
    onClick: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (item.itemType) {
                UiItemType.PLAYLIST -> if (ThemModeChanger.themMode) R.raw.lottie_playlist_dark else R.raw.lottie_playlist_light
                UiItemType.ALBUM -> if (ThemModeChanger.themMode) R.raw.lottie_album_dark else R.raw.lottie_album_light
                UiItemType.ARTIST -> if (ThemModeChanger.themMode) R.raw.lottie_artist_dark else R.raw.lottie_artist_light
                UiItemType.FAVOURITE -> if (ThemModeChanger.themMode) R.raw.lottie_favourite_dark else R.raw.lottie_favourite_light
            }
        )
    )

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        onClick = onClick
    ) {
        Box(
            Modifier.fillMaxSize(),
            Alignment.Center
        ) {
            LottieAnimation(
                modifier = Modifier.fillMaxSize(
                    when (item.itemType) {
                        UiItemType.PLAYLIST -> .5f
                        UiItemType.ALBUM -> .4f
                        UiItemType.ARTIST -> .7f
                        UiItemType.FAVOURITE -> .5f
                    }
                ),
                iterations = 20,
                composition = composition,
                alignment = Alignment.Center,
                renderMode = RenderMode.SOFTWARE,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "${item.itemCount} ${
                    when (item.itemType) {
                        UiItemType.PLAYLIST -> stringResource(R.string.saved_playlist)
                        UiItemType.ALBUM -> stringResource(R.string.save_album)
                        UiItemType.ARTIST -> stringResource(R.string.followed_artists)
                        UiItemType.FAVOURITE -> stringResource(R.string.favourite_songs)
                    }
                }",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}