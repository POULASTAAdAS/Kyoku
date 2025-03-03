package com.poulastaa.settings.presentation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.settings.presentation.components.DeleteAccount
import com.poulastaa.settings.presentation.components.HistoryCard
import com.poulastaa.settings.presentation.components.LogoutCard
import com.poulastaa.settings.presentation.components.ProfileHeading
import com.poulastaa.settings.presentation.components.ProfileImageCard
import com.poulastaa.settings.presentation.components.ThemModeChanger
import com.poulastaa.settings.presentation.components.ThemPicker

@Composable
internal fun SettingsExpandedScreen(
    state: SettingsUiState,
    onAction: (SettingsUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    var cardHeightDp by remember { mutableStateOf(0.dp) }

    Scaffold(
        topBar = {
            AppBasicTopBar(R.string.settings) {
                navigateBack()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(.4f)
                        .aspectRatio(2f)
                        .onSizeChanged { size ->
                            cardHeightDp = with(density) { size.height.toDp() }
                        },
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 0.dp
                    ),
                    onClick = {
                        onAction(SettingsUiAction.OnProfileClick)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.medium1)
                    ) {
                        ProfileHeading()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileImageCard(Modifier.aspectRatio(1f), state.user)

                            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                            Column(
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(
                                    text = stringResource(R.string.hi),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                                )

                                Spacer(Modifier.weight(.5f))

                                Text(
                                    text = state.user.username,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = stringResource(R.string.view_profile),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeightDp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ThemModeChanger(onAction)

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        ThemPicker(
                            Modifier
                                .fillMaxWidth()
                                .height(cardHeightDp / 3)
                        ) {
                            onAction(SettingsUiAction.OnThemPicToggle)
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    HistoryCard(
                        Modifier.fillMaxWidth(),
                        onAction = onAction
                    )
                }
            }

            Spacer(Modifier.weight(.5f))

            LogoutCard(
                Modifier
                    .fillMaxWidth(.4f)
                    .height(65.dp),
                haptic = haptic,
                isLogoutBottomSheetVisible = state.isLogoutBottomSheetVisible,
                onAction = onAction
            )

            Spacer(Modifier.weight(1f))

            DeleteAccount(
                modifier = Modifier
                    .fillMaxWidth(.2f)
                    .height(50.dp),
                onAction = onAction
            )
        }
    }
}