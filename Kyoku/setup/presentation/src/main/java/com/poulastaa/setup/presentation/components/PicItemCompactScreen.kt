package com.poulastaa.setup.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground
import com.poulastaa.setup.presentation.pic_artist.components.ArtistLoadingCard
import com.poulastaa.setup.presentation.pic_genre.UiGenre
import com.poulastaa.setup.presentation.pic_genre.component.GenreCard
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@Composable
fun <T : Any> PicItemCompactScreen(
    @StringRes title: Int,
    @StringRes lessSelected: Int,
    @StringRes label: Int,
    gridSize: Int,
    query: String,
    isMinLimitReached: Boolean,
    isMakingApiCall: Boolean,
    data: LazyPagingItems<T>,
    contentPadding: Dp = MaterialTheme.dimens.small1,
    itemLoadingContent: @Composable (searchHeight: Dp) -> Unit = {},
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
    onQueryChange: (data: String) -> Unit,
    onClearClick: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val density = LocalDensity.current
    var searchHeight by remember { mutableStateOf(0.dp) }

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
                    modifier = Modifier.fillMaxSize(),
                    targetState = data.loadState.refresh
                ) { loadingState ->
                    when (loadingState) {
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

                        LoadState.Loading -> itemLoadingContent(searchHeight)

                        is LoadState.NotLoading -> PicScreenItemList(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            searchBarHeight = searchHeight,
                            contentPadding = contentPadding,
                            gridSize = gridSize,
                            data = data,
                            itemContent = itemContent
                        )
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
                        PigScreenTopBar(title)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AppTextField(
                                modifier = Modifier.fillMaxWidth(.85f),
                                text = query,
                                onValueChange = onQueryChange,
                                label = stringResource(label),
                                trailingIcon = FilterArtistIcon,
                                isClearButtonEnabled = true,
                                onClearClick = onClearClick
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
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = isMinLimitReached.not()) {
                            Column {
                                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

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
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@PreviewLightDark
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
        PicItemCompactScreen(
            title = R.string.pic_genre_title,
            lessSelected = R.string.less_genre_selected,
            label = R.string.genre_label,
            gridSize = 2,
            query = "",
            isMinLimitReached = false,
            isMakingApiCall = false,
            data = mockLazyPagingItems,
            itemLoadingContent = {
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it)
                        .padding(MaterialTheme.dimens.small2)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 3
                ) {
                    Spacer(Modifier.aspectRatio(6f))

                    repeat(32) {
                        ArtistLoadingCard(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
                                .padding(4.dp)
                        )
                    }
                }
            },
            itemContent = { item ->
                GenreCard(
                    genre = item,
                    modifier = Modifier.height(80.dp),
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