package com.poulastaa.play.presentation.root_drawer.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.SearchIcon
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppbar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    TopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
        title = {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                color = MaterialTheme.colorScheme.primary.copy(.85f)
            )
        },
        navigationIcon = {
            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable(
                        onClick = {
                            onProfileClick()
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        indication = null,
                        interactionSource = null
                    ),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profileUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                imageVector = UserIcon,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(MaterialTheme.dimens.small3)
                            )
                        }
                    },
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                strokeWidth = 1.5.dp,
                                strokeCap = StrokeCap.Round,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(.8f)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            HomeAppbar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "Good Morning",
                profileUrl = "",
                onProfileClick = { }
            ) {}
        }
    }
}