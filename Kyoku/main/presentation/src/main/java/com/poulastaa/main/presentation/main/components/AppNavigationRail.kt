package com.poulastaa.main.presentation.main.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.CalenderIcon
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.DayIcon
import com.poulastaa.core.presentation.designsystem.ui.HomeIcon
import com.poulastaa.core.presentation.designsystem.ui.LibrarySelectedIcon
import com.poulastaa.core.presentation.designsystem.ui.MenuIcon
import com.poulastaa.core.presentation.designsystem.ui.NightIcon
import com.poulastaa.core.presentation.designsystem.ui.SettingsIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppCacheImage
import com.poulastaa.main.domain.model.AppNavigationRailScreen
import com.poulastaa.main.domain.model.isOpened
import com.poulastaa.main.presentation.main.MainUiAction
import com.poulastaa.main.presentation.main.MainUiState

private const val ANIMATION_TIME = 500

@Composable
internal fun AppNavigationRail(
    state: MainUiState,
    haptic: HapticFeedback,
    onAction: (MainUiAction) -> Unit,
) {
    var offset: Offset = remember { Offset(0f, 0f) }
    val rotate by animateFloatAsState(
        targetValue = if (state.navigationRailState.isOpened()) 0f else 180f,
        animationSpec = tween(durationMillis = ANIMATION_TIME)
    )

    Surface(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.medium1)
            .padding(vertical = MaterialTheme.dimens.medium1)
            .animateContentSize(
                animationSpec = tween(durationMillis = ANIMATION_TIME)
            )
            .fillMaxHeight()
            .wrapContentWidth()
            .clip(CircleShape)
            .border(
                width = 1.8.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            IconButton(
                onClick = {
                    onAction(MainUiAction.ToggleNavigationRail)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                interactionSource = null
            ) {
                AnimatedContent(
                    targetState = state.navigationRailState.isOpened(),
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(ANIMATION_TIME)) + scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(
                                ANIMATION_TIME,
                            )
                        )).togetherWith(fadeOut(animationSpec = tween(ANIMATION_TIME)))
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
                                .rotate(if (state.navigationRailState.isOpened()) 180f else 0f)
                                .padding(3.dp)
                        )

                        false -> Icon(
                            imageVector = MenuIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(if (state.navigationRailState.isOpened()) 180f else 0f)
                                .padding(3.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            AnimatedVisibility(
                modifier = Modifier,
                visible = state.navigationRailState.isOpened(),
                enter = fadeIn(animationSpec = tween(ANIMATION_TIME)) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(ANIMATION_TIME)
                ),
                exit = fadeOut(animationSpec = tween(ANIMATION_TIME)) + scaleOut(
                    targetScale = 0.92f,
                    animationSpec = tween(ANIMATION_TIME)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppCacheImage(
                        url = state.user.profilePic,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .aspectRatio(1f)
                            .clickable {
                                onAction(
                                    MainUiAction.NavigateToNavigationRailScreen(
                                        AppNavigationRailScreen.PROFILE
                                    )
                                )
                            },
                        errorIcon = UserIcon,
                        border = 1.4.dp,
                        borderColor = MaterialTheme.colorScheme.primary,
                        iconColor = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(R.string.profile),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.offset(
                            y = (-8).dp
                        )
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.large1))
                }
            }

            NavRailButton(
                title = stringResource(R.string.home_label),
                icon = HomeIcon,
                isSelected = state.navigationRailScreen == AppNavigationRailScreen.HOME,
                onClick = {
                    onAction(
                        MainUiAction.NavigateToNavigationRailScreen(
                            AppNavigationRailScreen.HOME
                        )
                    )
                }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            NavRailButton(
                title = stringResource(R.string.library_label),
                icon = LibrarySelectedIcon,
                iconPadding = 8.dp,
                isSelected = state.navigationRailScreen == AppNavigationRailScreen.LIBRARY,
                onClick = {
                    onAction(
                        MainUiAction.NavigateToNavigationRailScreen(
                            AppNavigationRailScreen.LIBRARY
                        )
                    )
                }
            )

            AnimatedVisibility(
                modifier = Modifier,
                visible = state.navigationRailState.isOpened(),
                enter = fadeIn(animationSpec = tween(ANIMATION_TIME)) + expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(ANIMATION_TIME)
                ),
                exit = fadeOut(animationSpec = tween(ANIMATION_TIME)) + shrinkVertically(
                    animationSpec = tween(ANIMATION_TIME),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(MaterialTheme.dimens.small3))

                    NavRailButton(
                        title = stringResource(R.string.history_label),
                        icon = CalenderIcon,
                        isSelected = state.navigationRailScreen == AppNavigationRailScreen.HISTORY,
                        onClick = {
                            onAction(
                                MainUiAction.NavigateToNavigationRailScreen(
                                    AppNavigationRailScreen.HISTORY
                                )
                            )
                        }
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small3))


                    NavRailButton(
                        title = stringResource(R.string.settings_label),
                        icon = SettingsIcon,
                        isSelected = state.navigationRailScreen == AppNavigationRailScreen.SETTINGS,
                        onClick = {
                            onAction(
                                MainUiAction.NavigateToNavigationRailScreen(
                                    AppNavigationRailScreen.SETTINGS
                                )
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {
                    onAction(
                        MainUiAction.NavigateToNavigationRailScreen(
                            screen = AppNavigationRailScreen.THEME,
                            offset = offset
                        )
                    )
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .onGloballyPositioned {
                        offset = Offset(
                            x = it.positionInWindow().x + it.size.width / 2,
                            y = it.positionInWindow().y + it.size.height / 2,
                        )
                    }
            ) {
                Icon(
                    imageVector = if (isSystemInDarkTheme() == ThemChanger.them) DayIcon else NightIcon,
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.NavRailButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    iconPadding: Dp = 4.dp,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(iconPadding)
        )
    }

    AnimatedVisibility(visible = isSelected) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.offset(
                y = (-6).dp
            )
        )
    }
}