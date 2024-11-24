package com.poulastaa.play.presentation.create_playlist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SearchIcon
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistSearchToBar(
    title: String,
    isSearchEnable: Boolean,
    searchQuery: String,
    focusRequester: FocusRequester,
    onEvent: (CreatePlaylistUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    CenterAlignedTopAppBar(
        title = {
            AnimatedContent(isSearchEnable, label = "") { state ->
                when (state) {
                    true -> {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged { state ->
                                    if (state.toString() == "Inactive") focusRequester.requestFocus()
                                },
                            value = searchQuery,
                            onValueChange = { onEvent(CreatePlaylistUiEvent.OnSearchQueryChange(it)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null
                                )
                            },
                            maxLines = 1,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                                focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                            ),
                            placeholder = {
                                Text(text = stringResource(id = R.string.search))
                            },
                            textStyle = TextStyle(
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    navigateBack()
                                }
                            )
                        )
                    }

                    false -> {
                        Text(
                            text = title.replaceFirstChar { it.uppercaseChar() },
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        navigationIcon = {
            AppBackButton(
                icon = if (isSearchEnable) CancelIcon else ArrowBackIcon
            ) {
                if (isSearchEnable) onEvent(CreatePlaylistUiEvent.OnSearchToggle)
                else navigateBack()
            }
        },
        actions = {
            if (!isSearchEnable) IconButton(
                onClick = {
                    onEvent(CreatePlaylistUiEvent.OnSearchToggle)
                }
            ) {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            CreatePlaylistSearchToBar(
                title = CreatePlaylistType.SEARCH.value,
                onEvent = {},
                isSearchEnable = false,
                searchQuery = "",
                focusRequester = remember { FocusRequester() }
            ) {}
        }
    }
}