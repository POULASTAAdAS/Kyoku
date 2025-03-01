package com.poulastaa.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.settings.presentation.components.DeleteAccount
import com.poulastaa.settings.presentation.components.HistoryCard
import com.poulastaa.settings.presentation.components.LogoutCard
import com.poulastaa.settings.presentation.components.ProfileHeading
import com.poulastaa.settings.presentation.components.ProfileImageCard
import com.poulastaa.settings.presentation.components.SettingsTopAppBar
import com.poulastaa.settings.presentation.components.ThemModeChanger
import com.poulastaa.settings.presentation.components.ThemPicker

@Composable
fun SettingsCompactScreen(
    state: SettingsUiState,
    onAction: (SettingsUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            SettingsTopAppBar {
                navigateBack()
            }
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
                    ProfileHeading()

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileImageCard(Modifier.size(100.dp), state.user)

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
                ThemModeChanger(onAction)

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                ThemPicker(
                    Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                )
            }

            HistoryCard(Modifier.fillMaxWidth(), onAction)

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            LogoutCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                isLogoutBottomSheetVisible = state.isLogoutBottomSheetVisible,
                haptic = haptic,
                onAction = onAction
            )

            Spacer(Modifier.weight(1f))

            DeleteAccount(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .navigationBarsPadding()
                    .fillMaxWidth(.6f)
                    .height(50.dp),
                onAction = onAction
            )
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