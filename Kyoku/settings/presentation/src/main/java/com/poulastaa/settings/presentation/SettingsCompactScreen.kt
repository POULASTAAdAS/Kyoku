package com.poulastaa.settings.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CalenderIcon
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.DayIcon
import com.poulastaa.core.presentation.designsystem.ui.LogoutIcon
import com.poulastaa.core.presentation.designsystem.ui.NightIcon
import com.poulastaa.core.presentation.designsystem.ui.ShowMoreIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.settings.presentation.components.ProfilePlaceHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCompactScreen(
    state: SettingsUiState,
    onAction: (SettingsUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    var offset: Offset = remember { Offset(0f, 0f) }
    val haptic = LocalHapticFeedback.current
    var dragScope by remember { mutableFloatStateOf(0f) }
    var cardMaxWidth by remember { mutableIntStateOf(0) }
    var sliderMaxWidth by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "${stringResource(R.string.setting)}s",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = CloseIcon,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                ),
                onClick = {
                    onAction(SettingsUiAction.OnProfileClick)
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.medium1)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .height(MaterialTheme.typography.headlineMedium.fontSize.value.dp)
                                .width(3.dp)
                        )

                        Spacer(Modifier.width(MaterialTheme.dimens.small3))

                        Text(
                            text = stringResource(R.string.profile_title),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.size(100.dp),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            SubcomposeAsyncImage(
                                model = state.user.profilePic,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                error = {
                                    ProfilePlaceHolder()
                                },
                                loading = {
                                    ProfilePlaceHolder()
                                }
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        Column {
                            Text(
                                text = stringResource(R.string.hi),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize
                            )

                            Text(
                                text = state.user.username,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = stringResource(R.string.view_profile),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    modifier = Modifier.onGloballyPositioned {
                        offset = Offset(
                            x = it.positionInWindow().x + it.size.width / 2,
                            y = it.positionInWindow().y + it.size.height / 2,
                        )
                    },
                    checked = ThemChanger.them,
                    onCheckedChange = {
                        onAction(SettingsUiAction.OnStartThemChange(offset))
                    },
                    thumbContent = {
                        AnimatedContent(
                            ThemChanger.them,
                            label = "switch thumb animation",
                        ) {
                            when (it) {
                                true -> Icon(
                                    imageVector = NightIcon,
                                    contentDescription = null,
                                    modifier = Modifier.padding(2.dp)
                                )

                                false -> Icon(
                                    imageVector = DayIcon,
                                    contentDescription = null,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = MaterialTheme.colorScheme.primary,

                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,

                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,

                        checkedIconColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedIconColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 0.dp
                    ),
                    onClick = {
                        // todo on profile click
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = MaterialTheme.dimens.small3,
                                horizontal = MaterialTheme.dimens.medium1
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.color_type_1),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        )

                        Spacer(Modifier.weight(1f))

                        Icon(
                            imageVector = ShowMoreIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                ),
                onClick = {
                    onAction(SettingsUiAction.OnHistoryClick)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.medium1),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp,
                        ),
                    ) {
                        Icon(
                            imageVector = CalenderIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(4.dp),
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }

                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))


                    Text(
                        text = stringResource(R.string.history_label),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }


            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .onGloballyPositioned {
                        cardMaxWidth = it.size.width
                    },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .align(Alignment.CenterEnd)
                            .onGloballyPositioned {
                                sliderMaxWidth = it.size.width - 8 // removing padding
                            },
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = LogoutIcon,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) {
                            Icon(
                                imageVector = ShowMoreIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .rotate(45f)
                                    .aspectRatio(1f)
                                    .padding(12.dp)
                            )
                        }
                    }

                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFCE9B99),
                            contentColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .offset {
                                IntOffset(dragScope.toInt(), 0)
                            }
                            .draggable(
                                state = rememberDraggableState {
                                    if (it > 0) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    dragScope = (it + dragScope).coerceAtLeast(0f)
                                },
                                orientation = Orientation.Horizontal,
                                onDragStopped = { _ ->
                                    dragScope = if (dragScope < cardMaxWidth / 2) 0f
                                    else {
//                                        onAction(SettingsUiAction.OpenLogoutDialog)
                                        (cardMaxWidth - (sliderMaxWidth * 2)).toFloat()
                                    }
                                }
                            )
                            .align(Alignment.CenterStart)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.logout_label),
                                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium3),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .fillMaxWidth(.6f)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
                    .navigationBarsPadding(),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.errorContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 0.dp
                ),
                onClick = {
                    onAction(SettingsUiAction.OpenDeleteAccountDialog)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.delete_account),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            SettingsCompactScreen(
                state = SettingsUiState(),
                onAction = {}, {}
            )
        }
    }
}