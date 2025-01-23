package com.poulastaa.setup.presentation.pic_genre

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import com.poulastaa.core.presentation.designsystem.SongIcon
import com.poulastaa.core.presentation.designsystem.components.AppLoadingButton
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.designsystem.gradiantBackground
import com.poulastaa.setup.presentation.pic_genre.component.PicGenreItemList
import com.poulastaa.setup.presentation.pic_genre.component.PigGenreFloatingActionButton
import com.poulastaa.setup.presentation.pic_genre.component.PigGenreTopBar

@Composable
fun PicGenreExtendedScreen(
    state: PicGenreUiState,
    gridSize: Int,
    cardHeight: Dp,
    genre: LazyPagingItems<UiGenre>,
    onAction: (PicGenreUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            PigGenreFloatingActionButton(state, onAction)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradiantBackground()
                    )
                )
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.45f)
                        .fillMaxHeight()
                        .padding(MaterialTheme.dimens.medium1),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(Modifier.height(MaterialTheme.dimens.large1))

                    AppLoadingButton(
                        modifier = Modifier.fillMaxWidth(.5f),
                        text = stringResource(R.string.search),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            focusManager.clearFocus()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                }

                AnimatedContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .padding(
                            bottom = MaterialTheme.dimens.small1,
                            end = MaterialTheme.dimens.small1
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraSmall
                        ),
                    targetState = genre.itemCount == 0
                ) { state ->
                    when (state) {
                        true -> Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(80.dp)
                            )
                        }

                        false -> PicGenreItemList(gridSize, genre, cardHeight, onAction)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = MaterialTheme.dimens.medium1)
                    .fillMaxWidth(.45f)
            ) {
                PigGenreTopBar()

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                AnimatedVisibility(visible = state.isMinLimitReached.not()) {
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

