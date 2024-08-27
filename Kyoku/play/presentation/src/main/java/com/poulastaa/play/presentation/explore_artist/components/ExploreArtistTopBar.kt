package com.poulastaa.play.presentation.explore_artist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.root_drawer.home.components.CircularArtist
import com.poulastaa.play.presentation.view_artist.components.FollowArtistButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreArtistTopBar(
    scroll: TopAppBarScrollBehavior,
    header: String,
    title: String,
    coverImage: String,
    isFollowed: Boolean,
    onFollowToggle: () -> Unit,
    navigateBack: () -> Unit
) {
    LargeTopAppBar(
        scrollBehavior = scroll,
        title = {
            Row(
                modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!scroll.isPinned) {
                    CircularArtist(
                        header = header,
                        url = coverImage,
                        modifier = Modifier.size(75.dp)
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))
                }

                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        navigationIcon = {
            AppBackButton(
                imageVector = CancelIcon,
                onClick = navigateBack
            )
        },
        actions = {
            AnimatedVisibility(visible = (!scroll.isPinned && title.isNotEmpty())) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.35f)
                        .padding(end = MaterialTheme.dimens.medium1)
                ) {
                    FollowArtistButton(
                        isArtistFollowed = isFollowed,
                        onClick = onFollowToggle
                    )
                }
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
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            ExploreArtistTopBar(
                title = "That Cool Artist",
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                coverImage = "",
                header = "",
                isFollowed = false,
                onFollowToggle = {}
            ) {

            }
        }
    }
}