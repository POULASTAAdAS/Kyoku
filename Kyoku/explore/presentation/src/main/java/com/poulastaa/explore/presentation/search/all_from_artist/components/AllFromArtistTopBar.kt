package com.poulastaa.explore.presentation.search.all_from_artist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFromArtistTopBar(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    artist: String,
    isSearchOpen: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    navigateBack: () -> Unit,
    onSearch: () -> Unit,
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scroll,
        title = {
            AnimatedContent(
                isSearchOpen,
                transitionSpec = {
                    (fadeIn(tween(400)) +
                            slideInVertically(tween(400)))
                        .togetherWith(fadeOut(tween(400)) + slideOutVertically(tween(400)))
                }
            ) {
                when (it) {
                    true -> {
                        OutlinedTextField(
                            value = query,
                            onValueChange = onQueryChange,
                            modifier = modifier,
                            shape = CircleShape,
                            label = {
                                Text(
                                    text = "search ${artist.lowercase()}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = SearchIcon,
                                    contentDescription = null,
                                    modifier = Modifier.noRippleClickable {
                                        onSearch()
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
                                    onSearch()
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

                    false -> Text(
                        text = "Explore $artist",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = Color.Transparent,
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AllFromArtistTopBar(
                modifier = Modifier.fillMaxWidth(),
                isSearchOpen = isSystemInDarkTheme(),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                artist = "That Cool Artist",
                query = "",
                onQueryChange = {},
                navigateBack = {},
                onSearch = {}
            )
        }
    }
}