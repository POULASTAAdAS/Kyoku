package com.poulastaa.setup.presentation.components

import android.content.res.Configuration
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.SadIcon
import com.poulastaa.core.presentation.ui.components.AppLoadingButton
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.setup.presentation.pic_genre.UiGenre
import com.poulastaa.setup.presentation.pic_genre.component.GenreCard
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@Composable
fun <T : Any> PicItemExtendedScreen(
    @StringRes title: Int,
    @StringRes lessSelected: Int,
    @StringRes label: Int,
    gridSize: Int,
    query: String,
    isMinLimitReached: Boolean,
    isMakingApiCall: Boolean,
    data: LazyPagingItems<T>,
    contentPadding: Dp = MaterialTheme.dimens.small1,
    itemLoadingContent: @Composable () -> Unit = {},
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
    onQueryChange: (data: String) -> Unit,
    onClearClick: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            PicScreenFloatingActionButton(
                isMakingApiCall = isMakingApiCall,
                isMinLimitReached = isMinLimitReached,
                onClick = onFloatingActionButtonClick
            )
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
                        text = query,
                        onValueChange = onQueryChange,
                        label = stringResource(label),
                        trailingIcon = FilterArtistIcon,
                        isClearButtonEnabled = true,
                        onClearClick = onClearClick
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
                    targetState = data.loadState.refresh
                ) { loadState ->
                    when (loadState) {
                        is LoadState.Error -> Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = SadIcon,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(.6f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        LoadState.Loading -> itemLoadingContent()

                        is LoadState.NotLoading -> PicScreenItemList(
                            modifier = Modifier.fillMaxSize(),
                            searchBarHeight = 0.dp,
                            contentPadding = contentPadding,
                            gridSize = gridSize,
                            data = data,
                            itemContent = itemContent
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = MaterialTheme.dimens.medium1)
                    .fillMaxWidth(.45f)
            ) {
                PigScreenTopBar(title)

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                AnimatedVisibility(visible = isMinLimitReached.not()) {
                    Text(
                        text = stringResource(lessSelected),
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

@Preview(
    widthDp = 840,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 540
)
@Composable
private fun Preview() {
    val mockPagingData = PagingData.from(
        (1..10).map {
            UiGenre(
                id = it,
                name = "Genre $it",
                isSelected = Random.nextBoolean()
            )
        }
    )
    val mockLazyPagingItems = flowOf(mockPagingData).collectAsLazyPagingItems()

    AppThem {
        PicItemExtendedScreen(
            title = R.string.pic_genre_title,
            lessSelected = R.string.less_genre_selected,
            label = R.string.genre_label,
            gridSize = 3,
            query = "",
            isMinLimitReached = false,
            isMakingApiCall = false,
            data = mockLazyPagingItems,
            itemContent = { item ->
                GenreCard(
                    genre = item,
                    modifier = Modifier.height(95.dp),
                    onClick = {

                    }
                )
            },
            onFloatingActionButtonClick = {},
            onQueryChange = {},
            onClearClick = {}
        )
    }
}