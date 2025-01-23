package com.poulastaa.setup.presentation.pic_genre

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SearchIcon
import com.poulastaa.core.presentation.designsystem.SongIcon
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.designsystem.gradiantBackground
import com.poulastaa.setup.presentation.pic_genre.component.PicGenreItemList
import com.poulastaa.setup.presentation.pic_genre.component.PigGenreFloatingActionButton
import com.poulastaa.setup.presentation.pic_genre.component.PigGenreTopBar

@Composable
internal fun PicGenreCompactScreen(
    state: PicGenreUiState,
    gridSize: Int,
    cardHeight: Dp,
    genre: LazyPagingItems<UiGenre>,
    onAction: (PicGenreUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val density = LocalDensity.current
    var searchHeight by remember { mutableStateOf(0.dp) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            PigGenreFloatingActionButton(state, onAction)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradiantBackground()
                    )
                )
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    modifier = Modifier
                        .align(Alignment.Center),
                    targetState = genre.itemCount == 0
                ) { state ->
                    when (state) {
                        true -> CircularProgressIndicator(
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)
                        )

                        false -> PicGenreItemList(gridSize, genre, cardHeight, onAction)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            searchHeight = with(density) {
                                it.size.height.toDp()
                            }
                        },
                    shape = RoundedCornerShape(
                        bottomEnd = 48.dp,
                        bottomStart = 48.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.medium1)
                            .padding(top = MaterialTheme.dimens.small3)
                            .fillMaxWidth()

                    ) {
                        PigGenreTopBar()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTextField(
                                modifier = Modifier.fillMaxWidth(.85f),
                                text = state.searchGenre.value,
                                onValueChange = {
                                    onAction(PicGenreUiAction.OnGenreChange(it))
                                },
                                label = stringResource(R.string.genre_label),
                                trailingIcon = SongIcon,
                                isClearButtonEnabled = true,
                                onClearClick = {
                                    onAction(PicGenreUiAction.OnGenreChange(""))
                                }
                            )

                            Spacer(Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    focusManager.clearFocus()
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.background
                                ),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(
                                    imageVector = SearchIcon,
                                    contentDescription = null
                                )
                            }
                        }

                        AnimatedVisibility(visible = state.isMinLimitReached.not()) {
                            Column {
                                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                                Text(
                                    text = stringResource(R.string.less_genre_selected),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

