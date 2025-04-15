package com.poulastaa.add.presentation.playlist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiAction
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AddSongToPlaylistSearchTopBar(
    query: String,
    filterType: AddSongToPlaylistSearchUiFilterType,
    isExtended: Boolean = false,
    onAction: (AddSongToPlaylistUiAction) -> Unit,
    focusManager: FocusManager,
    navigateBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isExtended) AddSongToPlaylistSearchFilterChips(
                    filterType = filterType,
                    onAction = onAction
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(tween(400)),
                    value = query,
                    onValueChange = {
                        onAction(AddSongToPlaylistUiAction.OnSearchQueryChange(it))
                    },
                    shape = CircleShape,
                    label = {
                        Text(
                            text = stringResource(R.string.search_anything),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = SearchIcon,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                focusManager.clearFocus()
                            }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                        showKeyboardOnFocus = true,
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (query.isNotEmpty()) {
                        onAction(AddSongToPlaylistUiAction.OnSearchQueryChange(""))
                    } else navigateBack()
                }
            ) {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.8f)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        ),
    )
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistSearchTopBar(
                query = "",
                onAction = {},
                filterType = AddSongToPlaylistSearchUiFilterType.ALL,
                focusManager = LocalFocusManager.current,
            ) { }
        }
    }
}