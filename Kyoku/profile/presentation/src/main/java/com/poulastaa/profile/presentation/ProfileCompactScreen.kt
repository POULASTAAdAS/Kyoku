package com.poulastaa.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.model.UiUser
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CalenderIcon
import com.poulastaa.core.presentation.designsystem.ui.EditIcon
import com.poulastaa.core.presentation.designsystem.ui.EmailAlternateIcon
import com.poulastaa.core.presentation.designsystem.ui.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterPlaylistIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.core.presentation.ui.components.MovingCirclesWithMetaballEffect

@OptIn(ExperimentalLayoutApi::class)
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
                        colors = ThemChanger.getGradiantBackground()
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(.4f)
                                .aspectRatio(1f)
                                .onSizeChanged {
                                    cardHeight = with(density) { it.height.toDp() + 32.dp }
                                },
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            )
                        ) {
                            SubcomposeAsyncImage(
                                model = state.user.profilePic,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                error = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = UserIcon,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(.75f)
                                        )
                                    }
                                },
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = UserIcon,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(.75f)
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 0.dp
                            ),
                            border = BorderStroke(
                                width = 2.5.dp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            onClick = {
                                onAction(ProfileUiAction.OnEditProfileImage)
                            }
                        ) {
                            Text(
                                text = "change profile image",
                                modifier = Modifier
                                    .padding(MaterialTheme.dimens.medium1)
                                    .padding(horizontal = MaterialTheme.dimens.small2),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(MaterialTheme.dimens.large1))

                OutlinedCard(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = UserIcon,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(MaterialTheme.dimens.small3))

                        Text(
                            text = state.user.username,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )

                        Spacer(Modifier.weight(1f))

                        Icon(
                            imageVector = EditIcon,
                            contentDescription = null,
                            modifier = Modifier.noRippleClickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onAction(ProfileUiAction.OnEditUserName)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    OutlinedCard(Modifier) {
                        Row(
                            modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = EmailAlternateIcon,
                                contentDescription = null
                            )

                            Spacer(Modifier.width(MaterialTheme.dimens.small3))

                            Text(state.user.email)
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    OutlinedCard(Modifier) {
                        Row(
                            modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = CalenderIcon,
                                contentDescription = null
                            )

                            Spacer(Modifier.width(MaterialTheme.dimens.small3))

                            Text(state.bDate)
                        }
                    }
                }


                Spacer(
                    Modifier
                        .padding(vertical = MaterialTheme.dimens.large2)
                        .fillMaxWidth(.4f)
                        .align(Alignment.CenterHorizontally)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                FlowRow(
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    state.savedItems.forEach { item ->
                        OutlinedCard(Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .noRippleClickable {
                                        onAction(ProfileUiAction.OnNavigateSavedItem(item.itemType))
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = when (item.itemType) {
                                            UiItemType.PLAYLIST -> FilterPlaylistIcon
                                            UiItemType.ALBUM -> FilterAlbumIcon
                                            UiItemType.ARTIST -> FilterArtistIcon
                                            UiItemType.FAVOURITE -> FavouriteIcon
                                        },
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize(.6f)
                                            .align(Alignment.Center),
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                }

                                Spacer(Modifier.width(MaterialTheme.dimens.small3))

                                Text(
                                    text = "${item.itemCount} ${
                                        when (item.itemType) {
                                            UiItemType.PLAYLIST -> stringResource(R.string.saved_playlist)
                                            UiItemType.ALBUM -> stringResource(R.string.save_album)
                                            UiItemType.ARTIST -> stringResource(R.string.followed_artists)
                                            UiItemType.FAVOURITE -> stringResource(R.string.favourite_songs)
                                        }
                                    }"
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(.45f)
                        .align(Alignment.CenterHorizontally),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp
                    ),
                    onClick = {
                        onAction(ProfileUiAction.OnNavigateToGallery)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.go_to_library),
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1)
                            .padding(horizontal = MaterialTheme.dimens.medium1)
                            .align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun OutlinedCard(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        border = BorderStroke(
            width = 2.5.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        shape = CircleShape,
        content = content
    )
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