package com.poulastaa.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.MovingCirclesWithMetaballEffect
import com.poulastaa.profile.presentation.components.ChangeProfileTextCard
import com.poulastaa.profile.presentation.components.EmailCard
import com.poulastaa.profile.presentation.components.GoToLibrary
import com.poulastaa.profile.presentation.components.ItemCard
import com.poulastaa.profile.presentation.components.OtherDetailsCard
import com.poulastaa.profile.presentation.components.ProfileImageCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileExpandedScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = ThemChanger.getGradiantBackground()
                    )
                )
        ) {
            MovingCirclesWithMetaballEffect()

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.45f)
                        .fillMaxHeight()
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileImageCard(
                            modifier = Modifier
                                .fillMaxSize(.4f)
                                .aspectRatio(1f),
                            url = state.user.profilePic
                        )

                        Spacer(Modifier.weight(1f))

                        ChangeProfileTextCard(onAction)
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.large1))

                    EmailCard(state.user.username, haptic, onAction)

                    Spacer(Modifier.height(MaterialTheme.dimens.small3))

                    OtherDetailsCard(state.user.email, state.bDate)
                }

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.8f),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(.5f),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
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
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
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
                }
            }

            TopAppBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = MaterialTheme.dimens.small1),
                title = {
                    Text(
                        text = stringResource(R.string.profile),
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        modifier = Modifier.padding(start = MaterialTheme.dimens.medium1)
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
                windowInsets = WindowInsets(0, 24, 0, 0),
            )
        }
    }
}

