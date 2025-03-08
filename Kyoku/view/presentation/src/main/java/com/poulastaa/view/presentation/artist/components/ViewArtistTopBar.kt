package com.poulastaa.view.presentation.artist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppCacheImage
import com.poulastaa.view.presentation.artist.UiViewArtist
import com.poulastaa.view.presentation.artist.ViewArtistUiAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    artist: UiViewArtist,
    onAction: (ViewArtistUiAction.OnFollowToggle) -> Unit,
    navigateBack: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppCacheImage(
                    url = artist.cover,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp),
                    errorIcon = FilterArtistIcon,
                    iconColor = MaterialTheme.colorScheme.primary,
                    borderColor = Color.Transparent,
                    shape = CircleShape
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Text(
                    text = artist.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = ArrowBackIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f)
                )
            }
        },
        actions = {
            Button(
                modifier = Modifier.animateContentSize(tween(400)),
                onClick = {
                    onAction(ViewArtistUiAction.OnFollowToggle)
                }
            ) {
                Text(
                    text = if (artist.isFollowing) stringResource(R.string.un_follow)
                    else stringResource(R.string.follow),
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistTopBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                artist = UiViewArtist(
                    name = "That Cool Artist",
                    isFollowing = isSystemInDarkTheme()
                ),
                onAction = {}
            ) {}
        }
    }
}