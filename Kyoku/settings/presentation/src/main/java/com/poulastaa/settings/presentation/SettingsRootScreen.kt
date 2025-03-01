package com.poulastaa.settings.presentation

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.settings.domain.model.SettingsAllowedNavigationScreens

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    onLogOut: () -> Unit,
    navigate: (screen: SettingsAllowedNavigationScreens) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(context)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val bottomSheet = rememberModalBottomSheetState(confirmValueChange = { !state.isLoading })

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val triple = ThemChanger.compute(
        density,
        configuration,
        state.offset,
        state.themChangeAnimationTime,
        resetAnimation = { viewModel.onAction(SettingsUiAction.ResetRevelAnimation) }
    )

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SettingsUiEvent.OnLogOutSuccess -> onLogOut()
            is SettingsUiEvent.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is SettingsUiEvent.Navigate -> navigate(event.screen)
        }
    }

    LaunchedEffect(
        state.isLogoutBottomSheetVisible,
        state.isDeleteAccountVBottomSheetVisible
    ) {
        if (state.isLogoutBottomSheetVisible || state.isDeleteAccountVBottomSheetVisible) bottomSheet.show()
        else if (!state.isLoading) bottomSheet.hide()
    }

    Box(Modifier.fillMaxSize()) {
        KyokuWindowSize(
            windowSizeClass = windowSize,
            compactContent = {
                SettingsCompactScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    navigateBack = navigateBack
                )
            },
            mediumContent = {

            },
            expandedContent = {

            }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (triple.revealSize.value > 0) {
                drawCircle(
                    color = triple.backgroundColor,
                    radius = triple.revealSize.value,
                    center = triple.center
                )
            }
        }
    }

    if (bottomSheet.isVisible) ModalBottomSheet(
        onDismissRequest = {
            if (!state.isLoading) viewModel.onAction(
                if (state.isLogoutBottomSheetVisible) SettingsUiAction.CancelLogoutDialog
                else SettingsUiAction.CancelDeleteAccountDialog
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = !state.isLoading,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.alpha(if (state.isLoading) 0f else 1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (state.isLogoutBottomSheetVisible) Text(
                        text = "Are you sure you want to log out ?",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ) else Text(
                        text = "Are you sure you want to delete your account ?\n All your data will be lost.",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.large1))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                viewModel.onAction(
                                    if (state.isLogoutBottomSheetVisible) SettingsUiAction.CancelLogoutDialog
                                    else SettingsUiAction.CancelDeleteAccountDialog
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.no).uppercase(),
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.onAction(
                                    if (state.isLogoutBottomSheetVisible) SettingsUiAction.OnLogoutDialog
                                    else SettingsUiAction.OnDeleteAccountDialog
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.yes).uppercase(),
                            )
                        }
                    }
                }

                CircularProgressIndicator(
                    modifier = Modifier
                        .alpha(if (state.isLoading) 1f else 0f)
                        .align(Alignment.Center)
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}