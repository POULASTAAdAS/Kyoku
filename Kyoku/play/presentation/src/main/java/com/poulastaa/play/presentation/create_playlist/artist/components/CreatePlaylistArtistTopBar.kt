package com.poulastaa.play.presentation.create_playlist.artist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.root_drawer.home.components.CircularArtist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistArtistTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = stringResource(R.string.artist),
    header: String,
    coverImageUrl: String,
    navigateBack: () -> Unit,
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularArtist(
                    header = header,
                    url = coverImageUrl,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        navigationIcon = {
            AppBackButton {
                navigateBack()
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            CreatePlaylistArtistTopBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                header = "",
                coverImageUrl = "",
                navigateBack = {}
            )
        }
    }
}