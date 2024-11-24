package com.poulastaa.play.presentation.profile

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.EditIcon
import com.poulastaa.core.presentation.designsystem.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.core.presentation.ui.imageReq

@Composable
fun ProfilePortraitRootScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLibrary: () -> Unit,
    navigateBack: () -> Unit,
) {
    ObserveAsEvent(viewModel.uiState) {
        when (it) {
            is ProfileUiAction.NavigateToLibrary -> navigateToLibrary()
        }
    }

    ProfilePortraitScreen(
        state = viewModel.state,
        navigateBack = navigateBack,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ProfilePortraitScreen(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_title),
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.small3)
                    )
                },
                navigationIcon = {
                    AppBackButton {
                        navigateBack()
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopCard(paddingValues, state, onEvent)
            NameCard(state)

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                ProfileItems(
                    icon = FilterArtistIcon,
                    value = state.savedArtistCount,
                    text = stringResource(R.string.followed_artists),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEvent(ProfileUiEvent.OnItemClick(ProfileItemType.ARTIST))
                    }
                )

                Spacer(Modifier.width(MaterialTheme.dimens.small1))

                ProfileItems(
                    icon = FilterAlbumIcon,
                    value = state.savedAlbumCount,
                    text = stringResource(R.string.saved_albums),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEvent(ProfileUiEvent.OnItemClick(ProfileItemType.ALBUM))
                    }
                )

                Spacer(Modifier.width(MaterialTheme.dimens.small1))

                ProfileItems(
                    icon = FilterPlaylistIcon,
                    value = state.savedPlaylistCount,
                    text = stringResource(R.string.saved_playlist),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEvent(ProfileUiEvent.OnItemClick(ProfileItemType.PLAYLIST))
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileLibraryButton(
                    haptic = haptic,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun ProfileLibraryButton(
    modifier: Modifier = Modifier,
    haptic: HapticFeedback,
    onEvent: (ProfileUiEvent) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(.6f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 0.dp
        ),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onEvent(ProfileUiEvent.OnItemClick(ProfileItemType.LIBRARY))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Go to ${stringResource(R.string.library_label).lowercase()}",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ProfileItems(
    value: Int,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val animatedValue by animateIntAsState(
        targetValue = value, label = "",
        animationSpec = tween(durationMillis = 500)
    )

    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 0.dp
        ),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .padding(start = 3.dp)
                    .clip(CircleShape)
                    .size(45.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(MaterialTheme.dimens.small3),
                tint = MaterialTheme.colorScheme.background
            )

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Text(
                text = "$animatedValue $text",
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(end = MaterialTheme.dimens.medium2),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TopCard(
    paddingValues: PaddingValues,
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.4f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary
        ),
        shape = RoundedCornerShape(
            bottomEnd = MaterialTheme.dimens.medium1,
            bottomStart = MaterialTheme.dimens.medium1
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MovingCirclesWithMetaballEffect()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileImage(state) {
                    onEvent(ProfileUiEvent.EditClick)
                }
            }
        }
    }
}

@Composable
fun NameCard(state: ProfileUiState) {
    Card(
        modifier = Modifier.Companion
            .offset(y = (-20).dp)
            .padding(horizontal = MaterialTheme.dimens.medium1),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        shape = CircleShape
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = MaterialTheme.dimens.small3,
                    horizontal = MaterialTheme.dimens.medium3
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                maxLines = 2
            )
        }
    }
}

@Composable
fun ProfileImage(
    state: ProfileUiState,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimens.medium1)
                .fillMaxSize(.6f)
                .aspectRatio(1f),
            shape = CircleShape,
            onClick = onClick,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            SubcomposeAsyncImage(
                model = imageReq(
                    header = state.header,
                    url = state.imageUrl
                ),
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                error = {
                    Icon(
                        imageVector = UserIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()

                            .padding(MaterialTheme.dimens.medium1)
                    )
                },
                loading = {
                    Icon(
                        imageVector = UserIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()

                            .padding(MaterialTheme.dimens.medium1)
                    )
                }
            )
        }

        IconButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(.6f)
                .fillMaxHeight(.4f),
        ) {
            Icon(
                imageVector = EditIcon,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(MaterialTheme.dimens.medium1),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        ProfilePortraitScreen(
            state = ProfileUiState(
                name = "Poulastaa Das"
            ),
            onEvent = {},
            navigateBack = {}
        )
    }
}