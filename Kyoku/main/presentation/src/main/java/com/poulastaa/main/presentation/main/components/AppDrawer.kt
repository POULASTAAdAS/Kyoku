package com.poulastaa.main.presentation.main.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CalenderIcon
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.DayIcon
import com.poulastaa.core.presentation.designsystem.ui.ListIcon
import com.poulastaa.core.presentation.designsystem.ui.NightIcon
import com.poulastaa.core.presentation.designsystem.ui.SettingsIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.core.presentation.ui.components.AppCacheImage
import com.poulastaa.main.domain.model.AppDrawerScreen
import com.poulastaa.main.presentation.main.MainUiAction

private const val ANIMATION_TIME = 600

@Composable
internal fun AppDrawer(
    isOpen: Boolean,
    user: UiUser,
    navigate: (MainUiAction.NavigateToDrawerScreen) -> Unit,
    onCloseClick: () -> Unit,
) {
    val rotate by animateFloatAsState(
        targetValue = if (isOpen) 0f else 180f,
        animationSpec = tween(durationMillis = ANIMATION_TIME, delayMillis = 120)
    )

    Box(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = gradiantBackground().map {
                        it.copy(alpha = .7f)
                    }
                )
            )
            .fillMaxSize()
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding()
            .systemBarsPadding()
            .alpha(if (isOpen) 1f else 0f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .fillMaxHeight()
        ) {
            IconButton(
                onClick = onCloseClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                AnimatedContent(
                    targetState = isOpen,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(ANIMATION_TIME, delayMillis = 120)) +
                                scaleIn(
                                    initialScale = 0.92f,
                                    animationSpec = tween(ANIMATION_TIME, delayMillis = 120)
                                ))
                            .togetherWith(fadeOut(animationSpec = tween(ANIMATION_TIME)))
                    },
                    modifier = Modifier.graphicsLayer {
                        rotationZ = rotate
                    }
                ) {
                    when (it) {
                        true -> Icon(
                            imageVector = CloseIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(if (isOpen) 180f else 0f)
                                .padding(3.dp)
                        )

                        false -> Icon(
                            imageVector = ListIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(if (isOpen) 180f else 0f)
                                .padding(3.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .clickable {
                        navigate(MainUiAction.NavigateToDrawerScreen(AppDrawerScreen.PROFILE))
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppCacheImage(
                    url = user.profilePic,
                    modifier = Modifier
                        .clip(CircleShape)
                        .weight(if (LocalConfiguration.current.screenWidthDp > 480) .3f else .4f)
                        .aspectRatio(1f),
                    errorIcon = UserIcon,
                    border = 2.dp,
                    borderColor = MaterialTheme.colorScheme.primary,
                    iconColor = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Column(
                    modifier = Modifier.weight(.6f)
                ) {
                    Text(
                        text = user.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = stringResource(R.string.view_profile),
                        color = MaterialTheme.colorScheme.primary.copy(.8f)
                    )
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.large2))

            DrawerItem(R.string.history_title, CalenderIcon) {
                navigate(MainUiAction.NavigateToDrawerScreen(AppDrawerScreen.HISTORY))
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            DrawerItem(R.string.settings_title, SettingsIcon) {
                navigate(MainUiAction.NavigateToDrawerScreen(AppDrawerScreen.SETTINGS))
            }

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {
                    navigate(MainUiAction.NavigateToDrawerScreen(AppDrawerScreen.THEME))
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.padding(MaterialTheme.dimens.medium1)
            ) {
                Icon(
                    imageVector = if (isSystemInDarkTheme()) DayIcon else NightIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }
        }
    }


}

@Composable
private fun DrawerItem(
    @StringRes
    title: Int,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = stringResource(title),
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick,
        selected = false,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = Color.Transparent
        )
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradiantBackground().map {
                            it.copy(alpha = .8f)
                        }
                    )
                )
        ) {
            AppDrawer(
                isOpen = true,
                user = UiUser(
                    username = "Poulastaa"
                ),
                navigate = {},
                onCloseClick = {}
            )
        }
    }
}