package com.poulastaa.view.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar

@Composable
fun ViewPlaylistFavouriteEmptyScreen(
    modifier: Modifier = Modifier,
    title: String,
    isPlaylist: Boolean,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    onCheckOutClick: () -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBasicTopBar(
                title = title,
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        ThemModeChanger.getGradiantBackground()
                    )
                )
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = verticalArrangement
        ) {
            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Card(
                modifier = Modifier
                    .size(200.dp),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaylist) SongIcon
                        else FavouriteIcon,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(.7f),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.large2))

            Text(
                text = if (isPlaylist) stringResource(R.string.build_your_playlist)
                else stringResource(R.string.build_your_favourite),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Button(
                onClick = onCheckOutClick
            ) {
                Text(
                    text = stringResource(R.string.check_out),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@PreviewLightDark
@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    val b = isSystemInDarkTheme()

    AppThem {
        ViewPlaylistFavouriteEmptyScreen(
            title = if (b) "That Cool Playlist" else "Favourite",
            isPlaylist = b,
            verticalArrangement = Arrangement.Center,
            onCheckOutClick = {},
            navigateBack = {}
        )
    }
}