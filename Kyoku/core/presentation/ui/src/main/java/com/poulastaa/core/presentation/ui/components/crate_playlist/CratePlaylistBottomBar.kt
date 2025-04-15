package com.poulastaa.core.presentation.ui.components.crate_playlist

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppTextField

@Composable
fun CratePlaylistBottomBar(
    created: (playlistId: PlaylistId) -> Unit,
    canceled: () -> Unit,
) {
    val context = LocalContext.current as Activity

    val viewmodel = hiltViewModel<CreatePlaylistBottomBarViewmodel>()
    ObserveAsEvent(viewmodel.uiEvent) { action ->
        when (action) {
            is CreatePlaylistUiEvent.EmitToast -> Toast.makeText(
                context,
                action.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is CreatePlaylistUiEvent.Created -> created(action.playlistId)
            CreatePlaylistUiEvent.Canceled -> canceled()
        }
    }

    val state by viewmodel.state.collectAsStateWithLifecycle()

    CreatePlaylist(
        state = state,
        onAction = viewmodel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylist(
    state: CreatePlaylistUiState,
    onAction: (CreatePlaylistUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    ModalBottomSheet(
        onDismissRequest = {
            if (state.isLoading.not()) onAction(CreatePlaylistUiAction.OnCancelClick)
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = state.isLoading.not(),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
                .padding(vertical = MaterialTheme.dimens.medium3)
        ) {
            AppTextField(
                text = state.name.value,
                onValueChange = { onAction(CreatePlaylistUiAction.OnPlaylistNameChange(it)) },
                label = stringResource(R.string.playlist),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = UserIcon,
                isError = state.name.isErr,
                supportingText = state.name.errText.asString(),
                isClearButtonEnabled = true,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onAction(CreatePlaylistUiAction.OnSaveClick)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ),
                onClearClick = { onAction(CreatePlaylistUiAction.OnPlaylistNameChange("")) }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            Row(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        focusManager.clearFocus()
                        onAction(CreatePlaylistUiAction.OnCancelClick)
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize(.8f),
                            imageVector = CloseIcon,
                            contentDescription = null
                        )
                    }
                }

                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    onClick = {
                        focusManager.clearFocus()
                        onAction(CreatePlaylistUiAction.OnSaveClick)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimatedContent(targetState = state.isLoading) {
                            when (it) {
                                true -> CircularProgressIndicator(
                                    modifier = Modifier.fillMaxSize(.6f),
                                    color = MaterialTheme.colorScheme.background,
                                    strokeWidth = 1.8.dp
                                )

                                false -> Icon(
                                    modifier = Modifier.fillMaxSize(.8f),
                                    imageVector = CheckIcon,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            CreatePlaylist(
                state = CreatePlaylistUiState(
                    isLoading = false
                )
            ) { }
        }
    }
}