package com.poulastaa.profile.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.core.presentation.ui.components.MovingCirclesWithMetaballEffect
import com.poulastaa.profile.presentation.components.ChangeProfileTextCard
import com.poulastaa.profile.presentation.components.EmailCard
import com.poulastaa.profile.presentation.components.GoToLibrary
import com.poulastaa.profile.presentation.components.ItemCard
import com.poulastaa.profile.presentation.components.OtherDetailsCard
import com.poulastaa.profile.presentation.components.ProfileImageCard

@Composable
internal fun ProfileCompactScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    var cardHeight by remember { mutableStateOf(0.dp) }

    Scaffold(
        topBar = {
            AppBasicTopBar(R.string.profile) {
                navigateBack()
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = ThemModeChanger.getGradiantBackground()
                    )
                )
        ) {
            MovingCirclesWithMetaballEffect()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.medium1),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ProfileImageCard(
                            Modifier
                                .fillMaxWidth(.4f)
                                .aspectRatio(1f)
                                .onSizeChanged {
                                    cardHeight = with(density) { it.height.toDp() + 32.dp }
                                },
                            state.user.profilePic
                        )

                        Spacer(Modifier.weight(1f))

                        ChangeProfileTextCard(onAction)
                    }
                }

                Spacer(Modifier.height(MaterialTheme.dimens.large1))

                EmailCard(state.user.username, haptic, onAction)

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                OtherDetailsCard(state.user.email, state.bDate)

                Spacer(
                    Modifier
                        .padding(vertical = MaterialTheme.dimens.large1)
                        .fillMaxWidth(.4f)
                        .align(Alignment.CenterHorizontally)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.5f),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        state.savedItems.take(2).forEach { item ->
                            ItemCard(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                item
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        state.savedItems.drop(2).forEach { item ->
                            ItemCard(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                item
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                GoToLibrary(
                    Modifier
                        .fillMaxWidth(.45f)
                        .align(Alignment.CenterHorizontally),
                    onAction
                )

                Spacer(Modifier.weight(1f))
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ProfileCompactScreen(
                state = ProfileUiState(
                    user = UiUser(
                        username = "Poulastaa",
                        email = "poulastaadas2@gmail.com",
                    ),
                    savedItems = listOf(
                        UiSavedItems(
                            itemType = UiItemType.PLAYLIST,
                            itemCount = 10
                        ),
                        UiSavedItems(
                            itemType = UiItemType.ARTIST,
                            itemCount = 10
                        ),
                        UiSavedItems(
                            itemType = UiItemType.ALBUM,
                            itemCount = 10
                        ),
                        UiSavedItems(
                            itemType = UiItemType.FAVOURITE,
                            itemCount = 10
                        )
                    )
                ),
                onAction = { }
            ) {}
        }
    }
}