package com.poulastaa.kyoku.presentation.screen.edit_playlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistLoadingScreen(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(30.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimens.small3)
                            .clip(CircleShape)
                            .shimmerEffect(),
                        onClick = navigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground.copy(.5f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .width(85.dp)
                    .height(38.dp)
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopPart()

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            repeat(4) {
                Item()
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
    }
}


@Composable
private fun TopPart() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(42.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmerEffect()
        )
    }
}


@Composable
fun Item() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.small)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(24.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(20.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        EditPlaylistLoadingScreen {

        }
    }
}