package com.poulastaa.play.presentation.add_as_playlist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.presentation.designsystem.AddAsPlaylistIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.components.RowButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import kotlinx.coroutines.launch

@Composable
fun AddAsPlaylistRootScreen(
    modifier: Modifier,
    viewModel: AddAsPlaylistViewModel = hiltViewModel(),
    exploreType: ExploreType,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = exploreType, key2 = Unit) {
        viewModel.populate(exploreType)
    }

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is AddAsPlaylistUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            AddAsPlaylistUiAction.Success -> {
                Toast.makeText(
                    context,
                    "$exploreType ${context.getString(R.string.added_as_playlist)}",
                    Toast.LENGTH_LONG
                ).show()

                navigateBack()
            }

            AddAsPlaylistUiAction.Cancel -> navigateBack()
        }
    }

    AddAsPlaylistScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@Composable
private fun AddAsPlaylistScreen(
    modifier: Modifier,
    state: AddAsPlaylistUiState,
    onEvent: (AddAsPlaylistUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Box(modifier = Modifier.fillMaxWidth()) {
            AppBackButton(
                imageVector = CancelIcon,
                onClick = navigateBack
            )

            Card(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                shape = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                ImageGrid(
                    header = state.header,
                    urls = state.prevSong.map { it.coverImage }
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.name,
            onValueChange = { onEvent(AddAsPlaylistUiEvent.OnNameChange(it)) },
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(text = stringResource(id = R.string.playlist))
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(AddAsPlaylistUiEvent.OnSubmit)
                }
            ),
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = AddAsPlaylistIcon,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

        RowButton(
            isMakingApiCall = state.isMakingApiCall,
            cancel = {
                focusManager.clearFocus()
                onEvent(AddAsPlaylistUiEvent.OnCancel)
            },
            save = {
                focusManager.clearFocus()
                onEvent(AddAsPlaylistUiEvent.OnSubmit)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        val sheet = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = true) {
            scope.launch {
                sheet.show()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ModalBottomSheet(
                onDismissRequest = { scope.launch { sheet.hide() } },
                sheetState = sheet
            ) {
                AddAsPlaylistScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.medium1)
                        .padding(bottom = MaterialTheme.dimens.medium1),
                    state = AddAsPlaylistUiState(
                        isMakingApiCall = true
                    ),
                    onEvent = {},
                ) {

                }
            }
        }
    }
}