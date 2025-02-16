package com.poulastaa.main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeCompactLoadingScreen(
    paddingValues: PaddingValues,
    scroll: TopAppBarScrollBehavior,
) {
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
    )

    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = gradiantBackground()
                )
            )
            .fillMaxSize()
            .nestedScroll(scroll.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets(top = MAIN_TOP_BAR_PADDING))
            .padding(paddingValues)
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        TopRow {
            repeat(2) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = cardColors
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    ) {
                        Card(
                            shape = CircleShape,
                            modifier = Modifier.aspectRatio(1f),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Shimmer()
                        }
                    }
                }
            }

        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        TopRow {
            repeat(2) {
                Card(
                    shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = cardColors
                ) {
                    Shimmer()
                }
            }
        }

        BoxItem(cardColors)

        Spacer(Modifier.height(MaterialTheme.dimens.large2))

        TextCard(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .height(30.dp)
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        ItemRow {
            repeat(10) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = cardColors
                ) {
                    Shimmer()
                }
            }
        }

        repeat(3) {
            BoxItem(cardColors)
        }

        Spacer(Modifier.height(MaterialTheme.dimens.large2))

        TextCard(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .height(30.dp)
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        repeat(5) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                            right = MaterialTheme.dimens.medium1
                        )
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                content = {
                    repeat(3) {
                        Card(
                            shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            colors = cardColors
                        ) {
                            Shimmer()
                        }
                    }
                }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))
        }

        Spacer(Modifier.height(BOTTOM_BAR_HEIGHT + MaterialTheme.dimens.medium3))
    }
}

@Stable
@Composable
private fun BoxItem(cardColors: CardColors) {
    Spacer(Modifier.height(MaterialTheme.dimens.large2))

    TextCard(
        modifier = Modifier
            .fillMaxWidth(.4f)
            .height(30.dp)
    )

    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

    ItemRow {
        repeat(10) {
            Card(
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                modifier = Modifier.aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                colors = cardColors
            ) {
                Shimmer()
            }
        }
    }
}

@Stable
@Composable
fun TopRow(
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = MaterialTheme.dimens.medium1),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
        content = content
    )
}


@Stable
@Composable
fun ItemRow(
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .horizontalScroll(rememberScrollState())
            .windowInsetsPadding(
                WindowInsets(
                    left = MaterialTheme.dimens.medium1,
                    right = MaterialTheme.dimens.medium1
                )
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        content = content
    )
}

@Stable
@Composable
private fun TextCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimens.medium1),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Shimmer()
    }
}

@Stable
@Composable
private fun Shimmer() {
    Box(
        Modifier
            .fillMaxSize()
            .shimmerEffect()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            HomeCompactLoadingScreen(
                PaddingValues(),
                TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    }
}