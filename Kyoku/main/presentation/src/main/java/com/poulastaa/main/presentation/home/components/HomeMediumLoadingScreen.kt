package com.poulastaa.main.presentation.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.LoadingScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMediumLoadingScreen(
    paddingValues: PaddingValues,
    scroll: TopAppBarScrollBehavior,
) {
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
    )

    LoadingScreenWrapper(paddingValues, scroll) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        HomeLoadingTopRow(
            itemHeight = 80.dp,
            itemSpacing = MaterialTheme.dimens.small2
        ) {
            repeat(3) {
                HomeLoadingAlbumCard(
                    Modifier.weight(1f),
                    cardColors,
                    CircleShape
                )
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        HomeLoadingTopRow(
            itemHeight = 80.dp,
            itemSpacing = MaterialTheme.dimens.small2
        ) {
            repeat(3) {
                HomeLoadingAlbumCard(
                    Modifier.weight(1f),
                    cardColors,
                    RoundedCornerShape(8.dp)
                )
            }
        }

        HomeLoadingCompactMediumCommon(cardColors)

        repeat(5) {
            HomeLoadingOtherCard(
                itemCount = 4,
                cardColors = cardColors
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))
        }

        Spacer(Modifier.height(BOTTOM_BAR_HEIGHT + MaterialTheme.dimens.medium3))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            HomeMediumLoadingScreen(
                PaddingValues(),
                TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    }
}