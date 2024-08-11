package com.poulastaa.play.presentation.root_drawer.components.compact

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CalenderIcon
import com.poulastaa.core.presentation.designsystem.LogoutIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SettingsIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent

@Composable
fun CompactDrawerContent(
    userName: String,
    profilePicUrl: String,
    navigate: (RootDrawerUiEvent.Navigate) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    ModalDrawerSheet(
        windowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal),
        drawerContainerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isSystemInDarkTheme()) listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primary.copy(.2f),
                        )
                        else listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primary.copy(.15f),
                        )
                    )
                )
                .padding(MaterialTheme.dimens.medium1)
                .safeDrawingPadding()
        ) {
            Profile(
                userName = userName,
                profilePicUrl = profilePicUrl,
                onProfileClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigate(RootDrawerUiEvent.Navigate(ScreenEnum.PROFILE))
                }
            )


            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.dimens.medium1)
                    .height(1.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.weight(.1f))

            DrawerItem(
                title = stringResource(id = R.string.history_title),
                icon = CalenderIcon,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigate(RootDrawerUiEvent.Navigate(ScreenEnum.HISTORY))
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            DrawerItem(
                title = stringResource(id = R.string.settings_title),
                icon = SettingsIcon,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigate(RootDrawerUiEvent.Navigate(ScreenEnum.SETTINGS))
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Profile(
    userName: String,
    profilePicUrl: String,
    onProfileClick: () -> Unit,
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                onProfileClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(.4f),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp,
            ),
            border = BorderStroke(
                width = 3.dp,
                color = MaterialTheme.colorScheme.primary.copy(.7f)
            )
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(profilePicUrl)
                    .placeholder(drawableResId = R.drawable.ic_user)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(.4f))
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .weight(.7f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userName,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

            Text(
                text = stringResource(id = R.string.view_profile),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(.6f)
            )
        }
    }
}


@Composable
private fun DrawerItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    colors: NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
        unselectedContainerColor = Color.Transparent,
        selectedContainerColor = Color.Transparent
    ),
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        modifier = modifier,
        label = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                letterSpacing = 1.5.sp
            )
        },
        selected = false,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        colors = colors
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        CompactDrawerContent(
            userName = "Poulastaa",
            profilePicUrl = "",
            navigate = {}
        )
    }
}