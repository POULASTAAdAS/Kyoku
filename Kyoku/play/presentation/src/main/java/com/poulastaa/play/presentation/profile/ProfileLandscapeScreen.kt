package com.poulastaa.play.presentation.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun ProfileLandscapeRootScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLibrary: () -> Unit,
    navigateBack: () -> Unit,
) {
    ObserveAsEvent(viewModel.uiState) {
        when (it) {
            is ProfileUiAction.NavigateToLibrary -> navigateToLibrary()
        }
    }

    ProfileLandscapeScreen(
        state = viewModel.state,
        navigateBack = navigateBack,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileLandscapeScreen(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.4f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.6f)
                            .padding(horizontal = MaterialTheme.dimens.medium1),
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

                    NameCard(state)
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2),
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

                        ProfileItems(
                            icon = FilterAlbumIcon,
                            value = state.savedAlbumCount,
                            text = stringResource(R.string.saved_albums),
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onEvent(ProfileUiEvent.OnItemClick(ProfileItemType.ALBUM))
                            }
                        )

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
                }
            }

            ProfileLibraryButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(.5f)
                    .padding(bottom = MaterialTheme.dimens.medium2),
                haptic = haptic,
                onEvent = onEvent
            )
        }
    }
}

@Preview(
    name = "Landscape Mode",
    heightDp = 490,
    widthDp = 940
)
@Preview(
    name = "Landscape Mode",
    heightDp = 490,
    widthDp = 940,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    AppThem {
        ProfileLandscapeScreen(
            state = ProfileUiState(
                name = "Poulastaa Das"
            ),
            onEvent = {},
            navigateBack = {}
        )
    }
}