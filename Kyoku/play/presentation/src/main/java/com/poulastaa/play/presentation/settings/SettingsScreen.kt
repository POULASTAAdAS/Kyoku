package com.poulastaa.play.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.LogoutIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.settings.components.LogoutDialog
import com.poulastaa.play.presentation.settings.components.SettingAccountCard
import com.poulastaa.play.presentation.settings.components.SettingBasicCard
import com.poulastaa.play.presentation.settings.components.SettingTopBar

@Composable
fun SettingsRootScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    navigate: (ScreenEnum) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is SettingUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is SettingUiAction.Navigate -> navigate(it.screen)
        }
    }

    SettingsScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingUiState,
    onEvent: (SettingUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            SettingTopBar(
                scrollBehavior = scrollBehavior,
                title = stringResource(R.string.setting)
            ) {
                navigateBack()
            }
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = MaterialTheme.dimens.medium1)
                .padding(it)
                .padding(top = MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            SettingAccountCard(
                profilePic = state.profilePicUrl,
                header = state.header,
                onAccountClick = {

                }
            )

            SettingBasicCard(
                text = stringResource(id = R.string.logout_label),
                icon = LogoutIcon,
                onClick = {
                    onEvent(SettingUiEvent.OnLogOutClick)
                }
            )
        }
    }

    if (state.isLogoutDialogVisible) LogoutDialog(
        isLoginOut = state.isLoggingOut,
        onConform = {
            onEvent(SettingUiEvent.OnLogOutConform)
        },
        onCancel = {
            onEvent(SettingUiEvent.OnLogOutCancel)
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        SettingsScreen(
            state = SettingUiState(),
            onEvent = {}) {

        }
    }
}