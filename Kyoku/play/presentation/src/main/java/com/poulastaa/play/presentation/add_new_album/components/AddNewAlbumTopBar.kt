package com.poulastaa.play.presentation.add_new_album.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAlbumTopBar(
    scroll: TopAppBarScrollBehavior,
    focusRequester: FocusRequester,
    isSearch: Boolean,
    searchQuery: String,
    isMassSelectEnabled: Boolean,
    onSearchChange: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    CenterAlignedTopAppBar(
        scrollBehavior = scroll,
        title = {
            AnimatedVisibility(
                visible = !isSearch && !isMassSelectEnabled,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                Text(
                    text = stringResource(id = R.string.explore_albums),
                    fontWeight = FontWeight.SemiBold
                )
            }

            AnimatedVisibility(
                visible = isSearch,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                        slideInVertically(animationSpec = tween(durationMillis = 500),
                            initialOffsetY = { -it / 2 }),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) +
                        slideOutVertically(animationSpec = tween(durationMillis = 500),
                            targetOffsetY = { -it / 2 })
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.toString() == "Inactive") focusRequester.requestFocus()
                        },
                    value = searchQuery,
                    onValueChange = onSearchChange,
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
                        )
                    ),
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_album))
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
        },
        navigationIcon = {
            AppBackButton(
                icon = if (isSearch || isMassSelectEnabled) CancelIcon else ArrowBackIcon,
                onClick = navigateBack
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    var search by remember { mutableStateOf(false) }

    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            AddNewAlbumTopBar(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                isSearch = search,
                focusRequester = remember { FocusRequester() },
                searchQuery = "",
                onSearchChange = {},
                isMassSelectEnabled = false,
                navigateBack = {
                    search = !search
                }
            )
        }
    }
}