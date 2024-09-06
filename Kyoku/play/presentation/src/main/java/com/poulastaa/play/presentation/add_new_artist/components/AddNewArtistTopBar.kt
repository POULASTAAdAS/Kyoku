package com.poulastaa.play.presentation.add_new_artist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewArtistTopBar(
    scroll: TopAppBarScrollBehavior,
    focusRequester: FocusRequester,
    isSearch: Boolean,
    searchQuery: String,
    isMassSelectEnabled: Boolean,
    isMakingApiCall: Boolean,
    onSearchChange: (String) -> Unit,
    onSaveClick: () -> Unit,
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
                    text = stringResource(id = R.string.explore_artist),
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
        ),
        actions = {
            AnimatedVisibility(isMassSelectEnabled) {
                Box(contentAlignment = Alignment.Center) {
                    OutlinedButton(
                        modifier = Modifier
                            .padding(end = MaterialTheme.dimens.small3)
                            .alpha(if (isMakingApiCall) 0f else 1f),
                        onClick = onSaveClick,
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(.7f)
                        )
                    ) {
                        Text(text = stringResource(R.string.save))
                    }

                    CircularProgressIndicator(
                        modifier = Modifier.alpha(if (isMakingApiCall) 1f else 0f)
                    )
                }
            }
        }
    )
}