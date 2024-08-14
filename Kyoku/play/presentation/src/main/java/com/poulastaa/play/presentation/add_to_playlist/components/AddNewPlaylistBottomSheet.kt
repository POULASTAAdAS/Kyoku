package com.poulastaa.play.presentation.add_to_playlist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AddAsPlaylistIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.components.RowButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.add_to_playlist.AddNewPlaylistBottomSheetUiState
import com.poulastaa.play.presentation.add_to_playlist.AddToPlaylistUiEvent
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPlaylistBottomSheet(
    sheetState: SheetState,
    state: AddNewPlaylistBottomSheetUiState,
    onEvent: (AddToPlaylistUiEvent.AddNewPlaylistUiEvent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnCancelClick) },
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            AnimatedVisibility(visible = !state.isValidName) {
                val temp = remember {
                    !state.isValidName
                }

                if (temp) Column {
                    Text(
                        text = state.errorMessage.asString(),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))
                }
            }

            AppTextField(
                text = state.newPlaylistName,
                onValueChange = { onEvent(AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnNameChange(it)) },
                label = stringResource(id = R.string.playlist),
                onDone = {
                    onEvent(AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnSaveClick)
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = AddAsPlaylistIcon,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            RowButton(
                isMakingApiCall = state.isMakingApiCall,
                cancel = {
                    onEvent(AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnCancelClick)
                },
                save = {
                    onEvent(AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnSaveClick)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    val err = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        (1..30).map {
            err.value = !err.value
            delay(3000)
        }
    }


    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AddNewPlaylistBottomSheet(
                sheetState = rememberModalBottomSheetState(),
                state = AddNewPlaylistBottomSheetUiState(
                    isAddNewPlaylistBottomSheetOpen = true,
                    isValidName = err.value,
                    errorMessage = UiText.DynamicString("Error message")
                ),
                onEvent = {}
            )
        }
    }
}