package com.poulastaa.add.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistSearchFilterChips
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AddSearchTopBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    label: String,
    query: String,
    isExtended: Boolean = false,
    focusManager: FocusManager,
    filterTypeContent: @Composable () -> Unit = {},
    onValueChange: (value: String) -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    navigateBack: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.animateContentSize(tween(600)),
        title = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isExtended) filterTypeContent()

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(tween(400)),
                    value = query,
                    onValueChange = onValueChange,
                    shape = CircleShape,
                    label = {
                        Text(
                            text = label,
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
                    if (query.isNotEmpty()) onValueChange("")
                    else navigateBack()
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
        scrollBehavior = scrollBehavior,
        actions = actions
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSearchTopBar(
                label = "search anything",
                query = "",
                isExtended = false,
                focusManager = LocalFocusManager.current,
                filterTypeContent = {},
                onValueChange = {},
                navigateBack = {}
            )
        }
    }
}

@Preview(
    widthDp = 1024,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewExpanded() {
    AppThem {
        Surface {
            AddSearchTopBar(
                label = "search anything",
                query = "",
                isExtended = isSystemInDarkTheme(),
                focusManager = LocalFocusManager.current,
                filterTypeContent = {
                    AddSongToPlaylistSearchFilterChips(
                        filterType = AddSongToPlaylistSearchUiFilterType.ALL,
                        onAction = { }
                    )
                },
                onValueChange = {},
                navigateBack = {}
            )
        }
    }
}