package com.poulastaa.play.presentation.root_drawer.components.expanded

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CalenderIcon
import com.poulastaa.core.presentation.designsystem.HomeSelectedIcon
import com.poulastaa.core.presentation.designsystem.HomeUnSelectedIcon
import com.poulastaa.core.presentation.designsystem.LibrarySelectedIcon
import com.poulastaa.core.presentation.designsystem.LibraryUnSelectedIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SettingsIcon
import com.poulastaa.core.presentation.designsystem.ThreeLineIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.presentation.root_drawer.RootDrawerUiEvent

@Composable
fun ExpandedDrawerContent(
    userName: String,
    profilePicUrl: String,
    isExpanded: Boolean,
    isExpandSmall: Boolean,
    saveScreen: SaveScreen,
    onExpandToggle: () -> Unit,
    onSaveScreenToggle: (SaveScreen) -> Unit,
    navigate: (RootDrawerUiEvent.Navigate) -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(MaterialTheme.dimens.medium2)
                .padding(vertical = MaterialTheme.dimens.small3)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardItem(
                modifier = Modifier
                    .rotate(
                        if (isExpanded) 90f else 0f
                    ),
                icon = ThreeLineIcon,
                isSelected = false,
                label = "",
                isExpandSmall = isExpandSmall,
                onClick = {
                    onExpandToggle()
                }
            )

            Profile(
                modifier = Modifier
                    .size(120.dp),
                isExpanded = isExpanded,
                userName = userName,
                profilePicUrl = profilePicUrl,
                onClick = {
                    if (isExpanded) onExpandToggle()
                    navigate(RootDrawerUiEvent.Navigate(ScreenEnum.PROFILE))
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            CardItem(
                icon = if (saveScreen == SaveScreen.HOME) HomeSelectedIcon else HomeUnSelectedIcon,
                label = stringResource(id = R.string.home_label),
                isExpandSmall = isExpandSmall,
                isSelected = saveScreen == SaveScreen.HOME,
                onClick = {
                    if (isExpanded) onExpandToggle()
                    onSaveScreenToggle(SaveScreen.HOME)
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            CardItem(
                icon = if (saveScreen == SaveScreen.LIBRARY) LibrarySelectedIcon else LibraryUnSelectedIcon,
                label = stringResource(id = R.string.library_label),
                isExpandSmall = isExpandSmall,
                isSelected = saveScreen == SaveScreen.LIBRARY,
                onClick = {
                    if (isExpanded) onExpandToggle()
                    onSaveScreenToggle(SaveScreen.LIBRARY)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Others(
                isExpanded = isExpanded,
                isExpandSmall = isExpandSmall,
                onHistoryClick = {
                    if (isExpanded) onExpandToggle()
                    navigate(RootDrawerUiEvent.Navigate(ScreenEnum.HISTORY))
                },
                onSettingsClick = {
                    if (isExpanded) onExpandToggle()
                    navigate(RootDrawerUiEvent.Navigate(ScreenEnum.SETTINGS))
                }
            )

            AnimatedVisibility(visible = !isExpanded) {
                CardItem(
                    icon = SettingsIcon,
                    label = stringResource(id = R.string.settings_label),
                    isExpandSmall = isExpandSmall,
                    shape = 30.dp,
                    isSelected = false,
                    onClick = {
                        navigate(RootDrawerUiEvent.Navigate(ScreenEnum.SETTINGS))
                    }
                )
            }
        }
    }
}

@Composable
private fun CardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isSelected: Boolean,
    label: String,
    isExpandSmall: Boolean,
    shape: Dp = if (isExpandSmall) 40.dp else 48.dp,
    onClick: () -> Unit,
) {
    var cardHeight by remember {
        mutableIntStateOf(0)
    }

    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = onClick
            )
            .onSizeChanged {
                cardHeight = it.height
            },
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = isSelected) {
                Row {
                    Spacer(
                        modifier = Modifier
                            .height((cardHeight / 3).dp)
                            .width(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(.7f))
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))
                }
            }


            Column(
                modifier = Modifier.padding(MaterialTheme.dimens.small1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(shape)
                        .then(modifier),
                )

                Text(text = label)
            }
        }
    }
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    userName: String,
    profilePicUrl: String,
    onClick: () -> Unit,
) {
    AnimatedVisibility(visible = isExpanded) {
        Column {
            Card(
                modifier = modifier,
                shape = MaterialTheme.shapes.large,
                onClick = onClick
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profilePicUrl)
                        .placeholder(R.drawable.ic_user)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(.7f)
                        .align(Alignment.CenterHorizontally)
                        .padding(MaterialTheme.dimens.small1)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(.3f)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(.7f),
                            shape = CircleShape
                        )
                )

                Text(
                    text = userName,
                    modifier = Modifier
                        .weight(.2f)
                        .align(Alignment.CenterHorizontally),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(id = R.string.view_profile),
                    modifier = Modifier
                        .weight(.2f)
                        .align(Alignment.CenterHorizontally),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }
    }
}

@Composable
fun Others(
    isExpanded: Boolean,
    isExpandSmall: Boolean,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    AnimatedVisibility(visible = isExpanded) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            CardItem(
                icon = CalenderIcon,
                label = stringResource(id = R.string.history_label),
                isExpandSmall = isExpandSmall,
                shape = 30.dp,
                isSelected = false,
                onClick = onHistoryClick
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            CardItem(
                icon = SettingsIcon,
                label = stringResource(id = R.string.settings_label),
                isExpandSmall = isExpandSmall,
                shape = 30.dp,
                isSelected = false,
                onClick = onSettingsClick
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        var state by remember {
            mutableStateOf(true)
        }

        ExpandedDrawerContent(
            userName = "Poulastaa",
            profilePicUrl = "",
            isExpandSmall = false,
            isExpanded = state,
            onExpandToggle = {
                state = !state
            },
            navigate = {},
            onSaveScreenToggle = {},
            saveScreen = SaveScreen.HOME
        )
    }
}