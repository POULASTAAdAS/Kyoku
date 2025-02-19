package com.poulastaa.main.presentation.library.components

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.home.components.LoadingScreenWrapper

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun LibraryCompactMediumLoadingScreen(
    scroll: TopAppBarScrollBehavior,
    paddingValues: PaddingValues,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
    )

    val defaultPadding = CardDefaults.cardElevation(
        defaultElevation = 6.dp
    )

    LoadingScreenWrapper(
        paddingValues = paddingValues,
        scroll = scroll,
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) {
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .width(70.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = cardColors,
                    elevation = defaultPadding
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Card(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                colors = cardColors,
                elevation = defaultPadding
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium2))

        LibraryLoadingRow(
            defaultPadding = defaultPadding,
            cardColors = cardColors,
            itemCount = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) 3 else 4
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium2))

        LibraryLoadingRow(
            defaultPadding = defaultPadding,
            cardColors = cardColors,
            itemCount = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) 4 else 5
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium2))

        LibraryLoadingRow(
            defaultPadding = defaultPadding,
            cardColors = cardColors,
            shapes = CircleShape,
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) 4 else 5
        )

        Spacer(Modifier.height(BOTTOM_BAR_HEIGHT + MaterialTheme.dimens.medium3))
    }
}

@Composable
private fun LibraryLoadingRow(
    defaultPadding: CardElevation,
    cardColors: CardColors,
    shapes: CornerBasedShape = MaterialTheme.shapes.extraSmall,
    itemCount: Int = 3
) {
    Column {
        Card(
            modifier = Modifier
                .height(35.dp)
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.medium1),
            elevation = defaultPadding,
            colors = cardColors,
            shape = CircleShape
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.small3))

        repeat(2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                repeat(itemCount) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        colors = cardColors,
                        elevation = defaultPadding,
                        shape = shapes
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small3))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            LibraryCompactMediumLoadingScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                paddingValues = PaddingValues()
            )
        }
    }
}