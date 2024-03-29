package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.shimmerEffect

@Composable
fun HomeScreenContentLoading(
    paddingValues: PaddingValues = PaddingValues(),
    isSmallPhone: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = paddingValues.calculateTopPadding() + MaterialTheme.dimens.small3,
                bottom = paddingValues.calculateBottomPadding()
            )
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2)
    ) {
        Column(
            modifier = Modifier.padding(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            if (isSmallPhone) Text(height = 24.dp) else Text(height = 38.dp, width = 240.dp)

            if (isSmallPhone) Box() else Box(size = 160.dp)

            if (isSmallPhone) Text(width = 120.dp) else Text(height = 24.dp, width = 160.dp)
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.padding(
                start = MaterialTheme.dimens.medium1
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            if (isSmallPhone) Text(height = 24.dp) else Text(height = 38.dp, width = 240.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        state = rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                repeat(times = 4) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isSmallPhone) Box() else Box(size = 160.dp)

                        if (isSmallPhone) Text(width = 120.dp)
                        else Text(height = 24.dp, width = 100.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        repeat(times = 3) {
            Column(
                modifier = Modifier.padding(
                    start = MaterialTheme.dimens.medium1
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                Row(
                    modifier = Modifier.height(if (isSmallPhone) 60.dp else 70.dp),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    if (isSmallPhone) Box(size = 60.dp, shape = CircleShape)
                    else Box(size = 70.dp, shape = CircleShape)

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (isSmallPhone) Text(height = 20.dp, width = 60.dp)
                        else Text(height = 24.dp, width = 60.dp)

                        if (isSmallPhone) Text(height = 24.dp, width = 90.dp)
                        else Text(height = 30.dp, width = 100.dp)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(
                            state = rememberScrollState()
                        ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    repeat(times = 4) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isSmallPhone) Box(size = 90.dp)
                            else Box(size = 120.dp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
        }
    }
}


@Composable
private fun Box(
    size: Dp = 120.dp,
    elevation: Dp = 5.dp,
    shape: CornerBasedShape = MaterialTheme.shapes.small
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = elevation,
                shape = shape
            )
            .shimmerEffect()
    )
}

@Composable
private fun Text(
    width: Dp = 180.dp,
    height: Dp = 20.dp,
    elevation: Dp = 5.dp,
    shape: CornerBasedShape = MaterialTheme.shapes.extraSmall
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .shadow(
                elevation = elevation,
                shape = shape
            )
            .shimmerEffect()
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    TestThem {
        HomeScreenContentLoading(
            isSmallPhone = true
        )
    }
}